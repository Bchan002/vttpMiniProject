package movieAppTracker.movieApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import movieAppTracker.movieApp.models.movieModel.Movie;
import movieAppTracker.movieApp.models.movieModel.MovieDetails;
import movieAppTracker.movieApp.models.movieModel.MovieReview;
import movieAppTracker.movieApp.service.movieService;

@Controller
@RequestMapping("/search")
public class searchMovieController {

    @Autowired
    private movieService movieSvc;

    @GetMapping("/movies")
    public String searchMovie(@RequestParam MultiValueMap<String, String> form, Model model, HttpSession session) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        String query = form.getFirst("query");
        session.setAttribute("query", query);
        List<Movie> movies;

        try {

            movies = movieSvc.searchMovie(query);
            model.addAttribute("movies", movies);
            return "movies";

        } catch (Exception e) {

            model.addAttribute("searchNotFound", "searchNotFound");
            return "movies";
        }

    }

    @GetMapping("/{id}")
    public String movieDetails(@PathVariable String id, Model model, HttpSession session) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {
            // System.out.println(id);
            List<MovieDetails> movieDetails = movieSvc.movieDetails(id);

            model.addAttribute("movie", movieDetails);
            model.addAttribute("id", id);

            // Get the query from session
            String query = (String) session.getAttribute("query");
            System.out.println(query);

            model.addAttribute("query", query);

            return "movieDetails";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";
        }

    }

    @GetMapping("/review/{id}")
    public String movieReview(@PathVariable String id, Model model, HttpSession session) throws Exception {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {

            List<MovieReview> movieReviews = movieSvc.movieReview(id);

            if (movieReviews.size() == 0) {
                model.addAttribute("noReview", true);
            } else {
                model.addAttribute("movieReviews", movieReviews);
            }

             // System.out.println(id);
            List<MovieDetails> movieDetails = movieSvc.movieDetails(id);

            model.addAttribute("movie", movieDetails);
            model.addAttribute("id", id);

            //Need to put in just now that session query 
            String query = (String) session.getAttribute("query");
            System.out.println(query);

            model.addAttribute("query", query);

            return "movieDetails";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erorr";
        }

        

         
    }

}
