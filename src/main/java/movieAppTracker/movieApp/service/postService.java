package movieAppTracker.movieApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.*;

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
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import movieAppTracker.movieApp.models.Comments;
import movieAppTracker.movieApp.models.watchList;
import movieAppTracker.movieApp.models.movieModel.Movie;
import movieAppTracker.movieApp.repository.postRepository;

@Service
public class postService {
    
    @Autowired
    private postRepository postRepo;


    @Value("${API_KEY}")
    private String API_KEY;

    private String SEARCH_URL = "https://api.themoviedb.org/3/movie";
    private String POSTER_URL ="https://image.tmdb.org/t/p/w342/";


    public void storeComments(String id, String username, String comments, int rating) throws Exception {
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
            .add("id", id)
            .add("title", object.getString("title"))
            .add("posterUrl", POSTER_URL + object.getString("poster_path"))
            .add("releaseDate", object.getString("release_date"))
            .build();

       //String movieId = String.valueOf(object.getJsonNumber("id").intValue());

       //1. Add movie details etc 
       postRepo.addMovie(username, id, object2);

       //Create a new JSon object for comments in redis 
       JsonObject object3 = Json.createObjectBuilder()
        .add("title", object.getString("title"))
        .add("username", username)
        .add("rating", rating)
        .add("comments", comments)
        .build();

        //2. Add comments to store in redis
        postRepo.addComments(username, id, object3);

        //3. For RestController: Store all comments 
        postRepo.addJsonComments(object3);

    }

    //Retrieve Movie by Key
    public List<Movie> retreiveMovie() throws Exception{

        List<Movie> movieList = new ArrayList<>();

        Optional<Map<String,Object>> map = postRepo.retreiveMovie();

        if(map.isEmpty()){
            throw new Exception("No comments stored");
        }

        Map<String,Object> map2 = map.get();

        for(String id: map2.keySet()){
            //cast it 
            String object = map2.get(id).toString();
            //System.out.println("yourObject is " + object);
            Reader reader = new StringReader(object);
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject object2 = jsonReader.readObject();

            System.out.println(object2.toString());
            Movie movie = new Movie();
            movie.setId(Integer.parseInt(object2.getString("id")));
            movie.setTitle(object2.getString("title"));
            movie.setPoster(object2.getString("posterUrl"));
            movie.setReleaseDate(object2.getString("releaseDate"));
            
           movieList.add(movie);
        }

        return movieList;

    }

    //Reteieve Comments by id 
    public List<Comments> retreiveComments(String id) throws Exception{

        List<Comments> commentList = new ArrayList<>();

        List<Object> commentObject = postRepo.retreiveComments(id);

        for(Object object: commentObject){
            String comments = object.toString();
            Reader reader = new StringReader(comments);
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject object2 = jsonReader.readObject();

            Comments comment = new Comments();
            comment.setTitle(object2.getString("title"));
            comment.setUsername(object2.getString("username"));
            comment.setComments(object2.getString("comments"));
            comment.setRatings(object2.getJsonNumber("rating").intValue());
                
            commentList.add(comment);
        }

        return commentList;

    }

    //Retrieve All comments for RestControlelr
    public JsonObject retreiveAllComments(){

       List<Object> commentObject = postRepo.retrieveJsonComments();

       List<Comments> commentsList = new ArrayList<>();

       for(Object object: commentObject){
            String comments = object.toString();
            Reader reader = new StringReader(comments);
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject object2 = jsonReader.readObject();

            Comments commentDetails = new Comments();
            commentDetails.setTitle(object2.getString("title"));
            commentDetails.setUsername(object2.getString("username"));
            commentDetails.setRatings(object2.getJsonNumber("rating").intValue());
            commentDetails.setComments(object2.getString("comments"));
            
            commentsList.add(commentDetails);
       }

       //Create a new Json Object to add everything in 
       JsonObjectBuilder object3 = Json.createObjectBuilder();

       for(int i =0; i<commentsList.size();i++){
            Comments commentAll = commentsList.get(i);
            JsonObject object4 = Json.createObjectBuilder()
                .add("title",commentAll.getTitle())
                .add("username", commentAll.getUsername())
                .add("rating",commentAll.getRatings())
                .add("reviews",commentAll.getComments())
                .build();
            
             object3.add("Review " + i ,object4);   
             
       }

       JsonObject object5 = object3.build();
     
        return object5;
       
    }
    
}
