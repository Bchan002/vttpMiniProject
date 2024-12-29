package movieAppTracker.movieApp.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;
import movieAppTracker.movieApp.models.watchList;
import movieAppTracker.movieApp.models.movieModel.Movie;
import movieAppTracker.movieApp.repository.watchlistRepository;

@Service
public class watchlistService {
    
    @Autowired
    private watchlistRepository watchListRepo;

    @Value("${API_KEY}")
    private String API_KEY;

    private String SEARCH_URL = "https://api.themoviedb.org/3/movie";
    private String POSTER_URL ="https://image.tmdb.org/t/p/w342/";

    //Search movie and store the details in redis 
    public void searchMovie(String id, String username) throws Exception{

        // Build the url
        String url = UriComponentsBuilder
                .fromUriString(SEARCH_URL + "/" + id )
                .queryParam("api_key", API_KEY)
                .toUriString();

        // System.out.println(url);
        RequestEntity<Void> req = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        RestTemplate template = new RestTemplate();


        ResponseEntity<String> resp;
        resp = template.exchange(req, String.class);

        if (resp.getStatusCode().is4xxClientError()) {
            throw new Exception("Authentication failed: Invalid movie name");
        }

        String payload = resp.getBody();
        Reader reader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(reader);

        JsonObject object = jsonReader.readObject();

        //Create a new JsonObject to store in redis 
        JsonObject object2 = Json.createObjectBuilder()
            .add("id", String.valueOf(object.getJsonNumber("id").intValue()))
            .add("title", object.getString("title"))
            .add("posterUrl", POSTER_URL + object.getString("poster_path"))
            .add("releaseDate", object.getString("release_date"))
            .add("watchStatus","Unwatched")
            .add("rating", 0)
            .build();

        //String movieId = String.valueOf(object.getJsonNumber("id").intValue());

        //Add to repo; 
        watchListRepo.addWatchList(username, id, object2);

    }

    //Retrieve watchList for user
    public List<watchList> retrieveWatchList(String username) throws Exception{

        List<watchList> watchList = new ArrayList<>();

        Optional<Map<String,Object>> map = watchListRepo.retreiveWatchList(username);

        if(map.isEmpty()){
            throw new Exception("No watchList stored");
        }

        Map<String,Object> map2 = map.get();

        for(String id: map2.keySet()){
            //cast it 
            String object = map2.get(id).toString();
            System.out.println("yourObject is " + object);
            Reader reader = new StringReader(object);
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject object2 = jsonReader.readObject();

            System.out.println(object2.toString());
            watchList list = new watchList();
            list.setId(object2.getString("id"));
            list.setTitle(object2.getString("title"));
            list.setPosterImage(object2.getString("posterUrl"));
            list.setDate(object2.getString("releaseDate"));
            list.setWatchStatus(object2.getString("watchStatus"));
            list.setRating(object2.getJsonNumber("rating").intValue());
             
            
            watchList.add(list);
        }

        return watchList;
        
    }

    public void removeWatchList(String username,String id){

        watchListRepo.removeWatchList(username, id);
    }


    public void updateWatchList(String username, String id, String watchStatus,int rating) throws Exception{

        String watchListDetails = watchListRepo.retrieveWatchLisByUsernameAndId(username, id);
        Reader reader = new StringReader(watchListDetails);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject object = jsonReader.readObject();

        System.out.println(object.toString());

         //Create a new JsonObject to store in redis 
         JsonObject object2 = Json.createObjectBuilder()
            .add("id", object.getString("id"))
            .add("title", object.getString("title"))
            .add("posterUrl", POSTER_URL + object.getString("posterUrl"))
            .add("releaseDate", object.getString("releaseDate"))
            .add("watchStatus",watchStatus)
            .add("rating",rating)
            .build();

        watchListRepo.addWatchList(username, id, object2);
        
    }

    public Boolean checkWatchListByUsernameAndId(String username,String id) throws Exception{
        
        String checkWatchList = watchListRepo.retrieveWatchLisByUsernameAndId(username, id); 

        if(checkWatchList==null){
            return false;
        }
        // if is not empty return true --> means already gt data so u dw to add already
        if(!checkWatchList.isEmpty()){
            return true;
        } 

        //is empty 
        return false;
    }

    public void removeAll(String username){
        watchListRepo.removeAllWatchList(username);
    }



}
