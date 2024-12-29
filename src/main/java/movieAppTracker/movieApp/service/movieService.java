package movieAppTracker.movieApp.service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import movieAppTracker.movieApp.models.movieModel.Movie;
import movieAppTracker.movieApp.models.movieModel.MovieDetails;
import movieAppTracker.movieApp.models.movieModel.MovieReview;

@Service
public class movieService {

    @Value("${API_KEY}")
    private String API_KEY;

    private String BASE_URL = "https://api.themoviedb.org/3/search/movie";
    private String POSTER_URL = "https://image.tmdb.org/t/p/w342/";
    private String POSTER_URL2 = "https://image.tmdb.org/t/p/original/";

    private String BASE_URL2 = "https://api.themoviedb.org/3/movie";

    private String REVIEW_URL = "https://api.themoviedb.org/3/movie";

    // Search movie
    public List<Movie> searchMovie(String movieName) throws Exception {

        // Create a new List of movies
        List<Movie> movies = new ArrayList<>();

        // Build the url
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("query", movieName)
                .queryParam("api_key", API_KEY)
                .toUriString();

        // System.out.println(url);

        // 1. Configure the request
        RequestEntity<Void> req = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        // 2. Create the restTemplate
        RestTemplate template = new RestTemplate();

        // 3. Configure the response
        ResponseEntity<String> resp;

        // Step 4
        resp = template.exchange(req, String.class);

        if (resp.getStatusCode().is4xxClientError()) {
            throw new Exception("Authentication failed: Invalid movie name");
        }

        // Step 5: Get the payload
        String payload = resp.getBody();

        // Step 6: Read the JSON format payload
        Reader reader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(reader);

        // Since it is an object
        JsonObject object = jsonReader.readObject();

        // Because the results is in an array
        JsonArray array = object.getJsonArray("results");

        if (array.isEmpty() || array == null) {
            throw new Exception("No search found");
        }

        // Iterate through the array
        for (int i = 0; i < array.size(); i++) {
            JsonObject object2 = array.getJsonObject(i);

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

            movies.add(movie);
        }

        return movies;

    }

    // Get movies Details
    public List<MovieDetails> movieDetails(String id) throws Exception {
        // Create a new List of Movie Details
        List<MovieDetails> movieDetails = new ArrayList<>();

        // Details url
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL2 + "/" + id)
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
            throw new Exception("Authentication failed, Please Try again");
        }

        String payload = resp.getBody();

        Reader reader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(reader);

        // Since it returns an Object
        JsonObject object = jsonReader.readObject();

        MovieDetails movieDetails2 = new MovieDetails();

        JsonArray genreArray = object.getJsonArray("genres");

        if (genreArray.isEmpty() || genreArray == null) {
            throw new Exception("Genre Array is null");
        }
        
        List<String> genreList = new ArrayList<>();
        for (int i = 0; i < genreArray.size(); i++) {
            String genre = genreArray.getJsonObject(i).getString("name");
            genreList.add(genre);
        }
        String genreList2 = genreList.toString();
        genreList2 = genreList2.replace("[", "");
        genreList2 = genreList2.replace("]", "");
        movieDetails2.setGenre(genreList2);
        movieDetails2.setLanguage(object.getString("original_language"));
        movieDetails2.setTitle(object.getString("original_title"));
        movieDetails2.setOverview(object.getString("overview"));
        String poster = POSTER_URL2 + object.getString("poster_path");
        movieDetails2.setImage(poster);
        movieDetails2.setReleaseDate(object.getString("release_date"));

        Long revenue = object.getJsonNumber("revenue").longValue();
        String formattedRevenue = "";
        if (revenue >= 1000000000) {
            double revenueInBillions = revenue / 1000000000.0;
            System.out.println(revenueInBillions);
            formattedRevenue = String.format("%.2f", revenueInBillions) + " billion USD";
        } else if (revenue >= 1000000) {
            double revenueInMillions = revenue / 1000000.0;
            formattedRevenue = String.format("%.2f", revenueInMillions) + " million USD";
        } else {
            formattedRevenue = "No revenue found";
        }

        movieDetails2.setRevenue(formattedRevenue);
        movieDetails2.setRating(object.getJsonNumber("vote_average").doubleValue());
        movieDetails2.setRuntime(object.getInt("runtime"));

        movieDetails.add(movieDetails2);

        return movieDetails;

    }

    // Get Movies Reviews
    public List<MovieReview> movieReview(String id) throws Exception {

        // "https://api.themoviedb.org/3/movie/{movie_id}/reviews";

        List<MovieReview> movieReviews = new ArrayList<>();

        String url = UriComponentsBuilder
                .fromUriString(REVIEW_URL + "/" + id + "/reviews")
                .queryParam("api_key", API_KEY)
                .toUriString();

        System.out.println(url);

        RequestEntity<Void> req = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp;

        resp = template.exchange(req, String.class);

        if (resp.getStatusCode().is4xxClientError()) {
            throw new Exception("Authentication failed, Please Try again");
        }

        String payload = resp.getBody();

        Reader reader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(reader);

        JsonObject object = jsonReader.readObject();

        JsonArray array = object.getJsonArray("results");

        for (int i = 0; i < array.size(); i++) {
            JsonObject object2 = array.getJsonObject(i);

            MovieReview review = new MovieReview();
            review.setAuthor(object2.getString("author"));

            JsonObject object3 = object2.getJsonObject("author_details");

            // Handle Rating
            JsonValue value = object3.get("rating");
            double rating;
            if (value == null || value == JsonValue.NULL) {
                review.setRating("No Rating Given");
            } else {
                JsonNumber number = (JsonNumber) value;
                rating = number.doubleValue();
                review.setRating(String.valueOf(rating));
            }
            review.setContent(object2.getString("content"));

            String date = object2.getString("created_at").substring(0, 10);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date formatDate = format.parse(date);

            String formattedDate = format.format(formatDate);
            review.setCreatedAt(formattedDate);

            movieReviews.add(review);

        }

        return movieReviews;

    }
}
