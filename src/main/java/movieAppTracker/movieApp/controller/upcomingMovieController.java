package movieAppTracker.movieApp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import movieAppTracker.movieApp.models.movieModel.Movie;
import movieAppTracker.movieApp.models.movieModel.MovieDetails;
import movieAppTracker.movieApp.models.movieModel.MovieReview;
import movieAppTracker.movieApp.service.movieService;
import movieAppTracker.movieApp.service.upcomingMovieService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/upcoming")
public class upcomingMovieController {

    @Autowired
    private upcomingMovieService upcomingMovieSvc;

    @Autowired
    private movieService movieSvc;

    @GetMapping("/movies")
    public String getUpcomingMovies(HttpSession session, Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pageNumbers.add(i);
        }

        try {

            List<Movie> upcomingMovies = upcomingMovieSvc.getUpcomingMovies(1);

            model.addAttribute("upComingMovies", upcomingMovies);
            model.addAttribute("pageNumbers", pageNumbers);
            // Default is 1
            model.addAttribute("currentPage", 1);

            // Initially Set as null
            session.setAttribute("filterMovieGenre", null);
            session.setAttribute("page", 1);

            return "upcomingMovies";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";

        }

    }

    @GetMapping("/movies/{page}")
    public String getUpcomingMoviesByPage(@PathVariable String page, HttpSession session, Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        // System.out.println(page);
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pageNumbers.add(i);
        }

        try {

            int pageNum = Integer.parseInt(page);
            List<Movie> upComingMovies = upcomingMovieSvc.getUpcomingMovies(pageNum);

            model.addAttribute("upComingMovies", upComingMovies);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("currentPage", Integer.parseInt(page));

            session.setAttribute("page", pageNum);
            return "upcomingMovies";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";
        }

    }

    @GetMapping("/filter")
    public String filterUpcomingMoviesByGenre(@RequestParam MultiValueMap<String, String> form, HttpSession session,
            Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pageNumbers.add(i);
        }

        try {

            String genre = form.getFirst("filterMovieGenre");
            System.out.println(genre);

            List<Movie> filteredMovie = upcomingMovieSvc.filterUpcomingMoviesGenre(1, genre);

            model.addAttribute("upComingMovies", filteredMovie);
            model.addAttribute("pageNumbers", pageNumbers);
            // Default is 1
            model.addAttribute("currentPage", 1);
            model.addAttribute("filterGenre", genre);

            session.setAttribute("filterMovieGenre", genre);
            session.setAttribute("page", 1);

            return "upcomingMovies";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";
        }
    }

    @GetMapping("/filterGenre")
    public String getFilteredGenrePage(@RequestParam MultiValueMap<String, String> form, HttpSession session,
            Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pageNumbers.add(i);
        }

        try {
            String genre = form.getFirst("filterMovieGenre");
            int pageNum = Integer.parseInt(form.getFirst("page"));

            List<Movie> filteredGenrePage = upcomingMovieSvc.filterUpcomingMoviesGenre(pageNum, genre);

            model.addAttribute("upComingMovies", filteredGenrePage);
            model.addAttribute("pageNumbers", pageNumbers);
            // Default is 1
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("filterGenre", genre);

            session.setAttribute("filterMovieGenre", genre);
            session.setAttribute("page", pageNum);

            return "upcomingMovies";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";
        }

    }

    @GetMapping("/movieDetails/{id}")
    public String getUpcomingMovieDetails(@PathVariable String id, HttpSession session, Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {
            // System.out.println(id);
            List<MovieDetails> movieDetails = movieSvc.movieDetails(id);

            model.addAttribute("movie", movieDetails);
            model.addAttribute("id", id);

            // Get filter and genre

            String filterMovieGenre = (String) session.getAttribute("filterMovieGenre");
            int page = (Integer) session.getAttribute("page");

            model.addAttribute("filterGenre", filterMovieGenre);
            model.addAttribute("page", page);

            return "upcomingMovieDetails";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";
        }

    }

    @GetMapping("/review/{id}")
    public String getUpcomingMovieReviews(@PathVariable String id, Model model, HttpSession session) {

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

            // Need to put in just now that session query
            
            String filterMovieGenre = (String) session.getAttribute("filterMovieGenre");
            int page = (Integer) session.getAttribute("page");

            model.addAttribute("filterGenre", filterMovieGenre);
            model.addAttribute("page", page);

            return "upcomingMovieDetails";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erorr";
        }
    }

}
