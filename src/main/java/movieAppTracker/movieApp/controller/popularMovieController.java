package movieAppTracker.movieApp.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import movieAppTracker.movieApp.models.movieModel.Movie;
import movieAppTracker.movieApp.models.movieModel.MovieDetails;
import movieAppTracker.movieApp.models.movieModel.MovieReview;
import movieAppTracker.movieApp.service.movieService;
import movieAppTracker.movieApp.service.popularMovieService;

@Controller
@RequestMapping("/popular")
public class popularMovieController {

    @Autowired
    private movieService movieSvc;


    @Autowired
    private popularMovieService popularMovieSvc;

    // "Landing page for movies";
    @GetMapping("/movies")
    public String getPopularMovies(HttpSession session, Model model) {

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pageNumbers.add(i);
        }

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {

            int pageNum = 1;
            List<Movie> popularMovies = popularMovieSvc.getPopularMovies(pageNum);

            model.addAttribute("popularMovies", popularMovies);
            model.addAttribute("pageNumbers", pageNumbers);
            // Default is 1
            model.addAttribute("currentPage", 1);
            model.addAttribute("firstFilter",null);
            model.addAttribute("filterMovieGenre",null);

            //Initially Set as null 
            session.setAttribute("firstFilter", null);
            session.setAttribute("filterMovieGenre",null);
            session.setAttribute("page",pageNum);


            return "popularMovies";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";
        }

    }

    @GetMapping("/movies/{page}")
    public String getPopularMoviesByPage(@PathVariable String page, HttpSession session, Model model) {

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
            List<Movie> popularMovies = popularMovieSvc.getPopularMovies(pageNum);

            model.addAttribute("popularMovies", popularMovies);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("currentPage", Integer.parseInt(page));

            session.setAttribute("page",pageNum);
            return "popularMovies";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";
        }

    }

    @GetMapping("/filterMovies")
    public String filterPopularMovies(@RequestParam MultiValueMap<String, String> form, HttpSession session,
            Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        String firstFilter = form.getFirst("filterMovie");
        String genre = form.getFirst("filterMovieGenre");

        try {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                pageNumbers.add(i);
            }

            // 1st check
            // firstFilter = "rating" , genre = "Horror"
            if (firstFilter != null && !firstFilter.isEmpty() && genre != null && !genre.isEmpty()) {

                System.out.println("first check");

                if (firstFilter.equals("rating")) {
                    String rating = "vote_average.desc";
                    List<Movie> filteredMovie = popularMovieSvc.filterBoth(1, rating, genre);
                    model.addAttribute("popularMovies", filteredMovie);
                    model.addAttribute("pageNumbers", pageNumbers);
                    // Default is 1
                    model.addAttribute("currentPage", 1);
                    model.addAttribute("filter", firstFilter);
                    model.addAttribute("filterMovieGenre", genre);

                    session.setAttribute("firstFilter", firstFilter);
                    session.setAttribute("filterMovieGenre",genre);
                    session.setAttribute("page",1);
                    return "popularMovies";

                } else if (firstFilter.equals("dateDesc")) {
                    String dateDesc = "primary_release_date.desc";
                    List<Movie> filteredMovie = popularMovieSvc.filterBoth(1, dateDesc, genre);
                    model.addAttribute("popularMovies", filteredMovie);
                    model.addAttribute("pageNumbers", pageNumbers);
                    // Default is 1
                    model.addAttribute("currentPage", 1);
                    model.addAttribute("filter", firstFilter);
                    model.addAttribute("filterMovieGenre", genre);

                    session.setAttribute("firstFilter", firstFilter);
                    session.setAttribute("filterMovieGenre",genre);
                    session.setAttribute("page",1);
                    return "popularMovies";
                }
                // return "Error";

            }

            // firstFilter = "rating" 
            // firstFilter = "date"
            if (firstFilter != null && !firstFilter.isEmpty() && (genre == null || genre.isEmpty())) {

                System.out.println("2nd Check");

                if (firstFilter.equals("rating")) {

                    String rating = "vote_average.desc";
                    List<Movie> filteredMovie = popularMovieSvc.filterByFirstFilter(1, rating);
                    model.addAttribute("popularMovies", filteredMovie);
                    model.addAttribute("pageNumbers", pageNumbers);
                    // Default is 1
                    model.addAttribute("currentPage", 1);
                    model.addAttribute("firstFilter", firstFilter);

                    session.setAttribute("firstFilter", firstFilter);
                    session.setAttribute("filterMovieGenre", null);
                    session.setAttribute("page",1);
                    return "popularMovies";

                } else if (firstFilter.equals("dateDesc")) {

                    String dateDesc = "primary_release_date.desc";
                    List<Movie> filteredMovie = popularMovieSvc.filterByFirstFilter(1, dateDesc);
                    model.addAttribute("popularMovies", filteredMovie);
                    model.addAttribute("pageNumbers", pageNumbers);
                    // Default is 1
                    model.addAttribute("currentPage", 1);
                    model.addAttribute("firstFilter", firstFilter);

                    session.setAttribute("firstFilter", firstFilter);
                    session.setAttribute("filterMovieGenre", null);
                    session.setAttribute("page",1);
                    return "popularMovies";
                }

                // return "Error";

            }

            if ((firstFilter == null || firstFilter.isEmpty()) && genre != null && !genre.isEmpty()) {

                System.out.println("third check");
                List<Movie> filteredMovie = popularMovieSvc.filterGenre(1, genre);
                model.addAttribute("popularMovies", filteredMovie);
                model.addAttribute("pageNumbers", pageNumbers);
                // Default is 1
                model.addAttribute("currentPage", 1);
                model.addAttribute("filterGenre", genre);

                session.setAttribute("firstFilter", null);
                session.setAttribute("filterMovieGenre", genre);
                session.setAttribute("page",1);
                return "popularMovies";

            }


            if (firstFilter == null || firstFilter.isEmpty() || genre == null || genre.isEmpty()) {

                //return back the original page
                System.out.println("fourth check");
                model.addAttribute("emptyFilter", true);
                int pageNum = 1;
                List<Movie> popularMovies = popularMovieSvc.getPopularMovies(pageNum);

                model.addAttribute("popularMovies", popularMovies);
                model.addAttribute("pageNumbers", pageNumbers);
                // Default is 1
                model.addAttribute("currentPage", 1);
                return "popularMovies";
            }

            return "Error";

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "Error";
        }

    }

    @GetMapping("/filteredMoviesByBoth")
    public String getFilteredMovies(@RequestParam MultiValueMap<String, String> form, Model model,
            HttpSession session) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pageNumbers.add(i);
        }

        try {

            String filterName = form.getFirst("filterMovie");
            String filterMovieGenre = form.getFirst("filterMovieGenre");
            int pageNum = Integer.parseInt(form.getFirst("page"));
            System.out.println("pageNumber" + pageNum);

            if (filterName == null) {

                return "popularMovies";

            } else if (filterName.equals("rating")) {

                String rating = "vote_average.desc";
                List<Movie> filteredMovie = popularMovieSvc.filterBoth(pageNum, rating, filterMovieGenre);
                model.addAttribute("popularMovies", filteredMovie);
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("currentPage", pageNum);
                model.addAttribute("filter", filterName);
                model.addAttribute("filterMovieGenre", filterMovieGenre);

                session.setAttribute("page", pageNum);

                return "popularMovies";

            } else if (filterName.equals("dateDesc")) {

                String dateDesc = "primary_release_date.desc";
                List<Movie> filteredMovie = popularMovieSvc.filterBoth(pageNum, dateDesc, filterMovieGenre);
                model.addAttribute("popularMovies", filteredMovie);
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("currentPage", pageNum);
                model.addAttribute("filter", filterName);
                model.addAttribute("filterMovieGenre", filterMovieGenre);

                session.setAttribute("page", pageNum);
                return "popularMovies";

            }

            return "popularMovies";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }

    }

    @GetMapping("filteredMoviesByFirstFilter")
    public String filterMoviesByFirstFilter(@RequestParam MultiValueMap<String, String> form, Model model,
            HttpSession session) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pageNumbers.add(i);
        }

        try {

            String firstFilter = form.getFirst("filterMovie");
            int pageNum = Integer.parseInt(form.getFirst("page"));

            if (firstFilter.equals("rating")) {

                String rating = "vote_average.desc";
                List<Movie> filteredMovie = popularMovieSvc.filterByFirstFilter(pageNum, rating);
                model.addAttribute("popularMovies", filteredMovie);
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("currentPage", pageNum);
                model.addAttribute("firstFilter", firstFilter);

                session.setAttribute("page", pageNum);

                return "popularMovies";

            }

            if (firstFilter.equals("dateDesc")) {

                String dateDesc = "primary_release_date.desc";
                List<Movie> filteredMovie = popularMovieSvc.filterByFirstFilter(pageNum, dateDesc);
                model.addAttribute("popularMovies", filteredMovie);
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("currentPage", pageNum);
                model.addAttribute("firstFilter", firstFilter);

                session.setAttribute("page", pageNum);

                return "popularMovies";

            }

            return "Error";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }

    }

    @GetMapping("/filteredMoviesByGenre")
    public String filterMoviesByGenre(@RequestParam MultiValueMap<String, String> form, Model model,
            HttpSession session) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pageNumbers.add(i);
        }

        try {

            String filterGenre = form.getFirst("filterGenre");
            int pageNum = Integer.parseInt(form.getFirst("page"));

            List<Movie> filteredMovie = popularMovieSvc.filterGenre(pageNum, filterGenre);
            model.addAttribute("popularMovies", filteredMovie);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("filterGenre", filterGenre);

            session.setAttribute("page", pageNum);

            return "popularMovies";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";
        }
    }


    @GetMapping("/movieDetails/{id}")
    public String getPopularMovieDetails(@PathVariable String id, Model model, HttpSession session){

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {
            // System.out.println(id);
            List<MovieDetails> movieDetails = movieSvc.movieDetails(id);

            model.addAttribute("movie", movieDetails);
            model.addAttribute("id", id);

            // Get filter and genre
            String firstFilter = (String) session.getAttribute("firstFilter");
            String filterMovieGenre = (String) session.getAttribute("filterMovieGenre");
            int page = (Integer) session.getAttribute("page");
            

            model.addAttribute("firstFilter", firstFilter);
            model.addAttribute("filterMovieGenre", filterMovieGenre);
            model.addAttribute("page", page);

            return "popularMovieDetails";

        } catch (Exception e) {

            e.printStackTrace();
            return "Error";
        }

    }


    @GetMapping("/review/{id}")
    public String getPopularMovieReviews(@PathVariable String id, Model model, HttpSession session){

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
            String firstFilter = (String) session.getAttribute("firstFilter");
            String filterMovieGenre = (String) session.getAttribute("filterMovieGenre");
            int page = (Integer) session.getAttribute("page");
           
            model.addAttribute("firstFilter", firstFilter);
            model.addAttribute("filterMovieGenre", filterMovieGenre);
            model.addAttribute("page", page);

            return "popularMovieDetails";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erorr";
        }
    }
}
