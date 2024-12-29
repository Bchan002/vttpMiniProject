package movieAppTracker.movieApp.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import movieAppTracker.movieApp.models.movieModel.Movie;
import movieAppTracker.movieApp.repository.guessMovieRepository;

@Service
public class guessMovieService {

    @Autowired
    private guessMovieRepository guessMovieRepo;


    @Value("${API_KEY}")
    private String API_KEY;

    private String POPULAR_MOVIE_URL = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&vote_average.lte=10&vote_count.gte=1000&primary_release_date.gte=2000-01-01&primary_release_date.lte=2024-11-30";

    public void storePopularMovie() throws Exception {

        int count =1;
        for (int i = 1; i <= 20; i++) {

            // Create the url
            String url = UriComponentsBuilder
                    .fromUriString(POPULAR_MOVIE_URL)
                    .queryParam("page", i)
                    .queryParam("api_key", API_KEY)
                    .toUriString();

            //System.out.println(url);

            RequestEntity<Void> req = RequestEntity
                    .get(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .build();

            // Rest Template
            RestTemplate template = new RestTemplate();

            // Configure response
            ResponseEntity<String> resp;

            resp = template.exchange(req, String.class);

            if (resp.getStatusCode().is4xxClientError()) {
                throw new Exception("Authentication failed: Invalid movie name");
            }

            String payload = resp.getBody();

            Reader reader = new StringReader(payload);
            JsonReader jsonReader = Json.createReader(reader);

            JsonObject object = jsonReader.readObject();

            JsonArray array = object.getJsonArray("results");

            for (int j = 0; j < array.size(); j++) {
                JsonObject object2 = array.getJsonObject(j);

                //Create a new JSON object 
                JsonObject object3 = Json.createObjectBuilder()
                    .add("title", object2.getString("title"))
                    .add("overview", object2.getString("overview"))
                    .build();

                guessMovieRepo.storeGuessMovieTitle(count,object3);
                count++;
            }

        }

    }

    public List<Movie> retreiveMovie() throws Exception{

        List<Movie> movieList = new ArrayList<>();

        Optional<Map<String,Object>> map = guessMovieRepo.retreiveMovie();

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

            //System.out.println(object2.toString());
            Movie movie = new Movie();
            movie.setTitle(object2.getString("title"));
            movie.setOverview(object2.getString("overview"));
            
           movieList.add(movie);
        }

        return movieList;

    }

    public List<String> getRandomMovieTitlesLow(List<Movie> movie){

        List<String> randomMovie = new ArrayList<>();

        Movie guessMovie = movieSelectLow(movie);
        
        //Get movie Title and overview 
        String movieTitle = guessMovie.getTitle();

        movieTitle = movieTitle.toUpperCase();
        System.out.println(movieTitle);
        //movieTitle = movieTitle.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String movieOverview = guessMovie.getOverview();

        randomMovie.add(movieTitle);
        randomMovie.add(movieOverview);

        return randomMovie;
    }

    //Call recursively to get movieLength less than 5
    public Movie movieSelectLow(List<Movie> movie){
        
        Random random = new Random();
        int rand = random.nextInt(0,movie.size());

        Movie guessMovie = movie.get(rand);
        String movieTitle = guessMovie.getTitle();
        System.out.println(movieTitle.length());

        if(movieTitle.length()>5){
           return movieSelectLow(movie);
        }

        return guessMovie;

    }   


    public List<String> getRandomMovieTitlesMedium(List<Movie> movie) throws Exception{

        List<String> randomMovie = new ArrayList<>();

        Movie guessMovie = movieSelectMedium(movie);
        
        //Get movie Title and overview 
        String movieTitle = guessMovie.getTitle();

        movieTitle = movieTitle.toUpperCase();
        System.out.println(movieTitle);
        String movieOverview = guessMovie.getOverview();

        randomMovie.add(movieTitle);
        randomMovie.add(movieOverview);

        return randomMovie;
    }

    //Call recursively to get movieLength not less than 10
    public Movie movieSelectMedium(List<Movie> movie) throws Exception{
        
        Random random = new Random();
        int rand = random.nextInt(0,movie.size());

        Movie guessMovie = movie.get(rand);
        String movieTitle = guessMovie.getTitle();
        System.out.println(movieTitle.length());

        if(movieTitle.length()>10){
           return movieSelectMedium(movie);
        }

        return guessMovie;

    }  

    public List<String> getRandomMovieTitlesHard(List<Movie> movie) throws Exception{

        List<String> randomMovie = new ArrayList<>();

        Movie guessMovie = movieSelectHard(movie);
        
        //Get movie Title and overview 
        String movieTitle = guessMovie.getTitle();

        movieTitle = movieTitle.toUpperCase();
        System.out.println(movieTitle);
        String movieOverview = guessMovie.getOverview();

        randomMovie.add(movieTitle);
        randomMovie.add(movieOverview);

        return randomMovie;
    }

    //Call recursively to get movieLength not less than 20
    public Movie movieSelectHard(List<Movie> movie) throws Exception{
        
        Random random = new Random();
        int rand = random.nextInt(0,movie.size());

        Movie guessMovie = movie.get(rand);
        String movieTitle = guessMovie.getTitle();
        System.out.println(movieTitle.length());

        if(movieTitle.length()>20){
           return movieSelectHard(movie);
        }

        return guessMovie;

    }  




    public Boolean checkWords(String userInput, String movieTitle){

        movieTitle = movieTitle.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        userInput = userInput.toUpperCase();

        if(userInput.equals(movieTitle)){
            return true;
        }

        return false;
    }
}
