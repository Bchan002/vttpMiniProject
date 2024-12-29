package movieAppTracker.movieApp.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

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

@Service
public class upcomingMovieService {
    
    @Value("${API_KEY}")
    private String API_KEY;

    private String UPCOMING_MOVIE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=cfdc24acca05eeff39c401913ec7671f&primary_release_date.gte=2024-12-25&primary_release_date.lte=2025-06-15";
    private String POSTER_URL = "https://image.tmdb.org/t/p/w342/";


    public List<Movie> getUpcomingMovies(int pageNum) throws Exception {

        // Create a new List of movies
        List<Movie> upcomingMovies = new ArrayList<>();

        String url = UriComponentsBuilder
                .fromUriString(UPCOMING_MOVIE_URL)
                .queryParam("page", pageNum)
                .queryParam("api_key", API_KEY)
                .toUriString();

        System.out.println(url);

        // Configure request
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

            Movie movie = new Movie();
            movie.setId(object2.getInt("id"));
            movie.setOverview(object2.getString("overview"));
            String poster;
            // Check if poster is null
            // Check if the "poster_path" key exists and is not null
            if (!object2.isNull("poster_path") && object2.getString("poster_path") != null) {
                poster = POSTER_URL + object2.getString("poster_path");
            } else {
                poster = "https://picsum.photos/300/450"; // Placeholder image URL
            }
            movie.setPoster(poster);
            movie.setRating(object2.getJsonNumber("vote_average").doubleValue());
            // Handle date
            movie.setReleaseDate(object2.getString("release_date"));
            movie.setTitle(object2.getString("title"));

            upcomingMovies.add(movie);

        }

        return upcomingMovies;

    }

    public List<Movie> filterUpcomingMoviesGenre(int pageNum,String genre) throws Exception {

        // Create a new List of movies
        List<Movie> upcomingMovies = new ArrayList<>();

        String url = UriComponentsBuilder
                .fromUriString(UPCOMING_MOVIE_URL)
                .queryParam("page", pageNum)
                .queryParam("api_key", API_KEY)
                .queryParam("with_genres", genre)
                .toUriString();

        System.out.println(url);

        // Configure request
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

            Movie movie = new Movie();
            movie.setId(object2.getInt("id"));
            movie.setOverview(object2.getString("overview"));
            String poster;
            // Check if poster is null
            // Check if the "poster_path" key exists and is not null
            if (!object2.isNull("poster_path") && object2.getString("poster_path") != null) {
                poster = POSTER_URL + object2.getString("poster_path");
            } else {
                poster = "https://picsum.photos/300/450"; // Placeholder image URL
            }
            movie.setPoster(poster);
            movie.setRating(object2.getJsonNumber("vote_average").doubleValue());
            // Handle date
            movie.setReleaseDate(object2.getString("release_date"));
            movie.setTitle(object2.getString("title"));

            upcomingMovies.add(movie);

        }

        return upcomingMovies;

    }

    

}
