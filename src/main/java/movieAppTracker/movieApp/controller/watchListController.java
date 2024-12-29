package movieAppTracker.movieApp.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import movieAppTracker.movieApp.models.Notes;
import movieAppTracker.movieApp.models.watchList;
import movieAppTracker.movieApp.service.watchlistService;

@Controller
@RequestMapping("/watchlist")
public class watchListController {

    @Autowired
    private watchlistService watchlistSvc;

    @PostMapping("/{id}")
    public String addWatchlist(@PathVariable String id, HttpSession session, Model model) throws Exception {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {

            String username = (String) session.getAttribute("username");
            String query = (String) session.getAttribute("query");

            // Check whether there is any watchlist already added
            if (watchlistSvc.checkWatchListByUsernameAndId(username, id)) {
                model.addAttribute("query", query);
                return "addedWatchlist";
            }

            return "Error";

        } catch (Exception e) {

            // if is null -> means no watchList added, add it to watchlist
            String username = (String) session.getAttribute("username");
            String query = (String) session.getAttribute("query");

            watchlistSvc.searchMovie(id, username);
            model.addAttribute("query", query);

            return "successfulWatchlist";
        }

    }

    @PostMapping("/popular/{id}")
    public String addPopularWatchlist(@PathVariable String id, HttpSession session, Model model) throws Exception {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {

            String username = (String) session.getAttribute("username");
            // Get filter and genre
            String firstFilter = (String) session.getAttribute("firstFilter");
            String filterMovieGenre = (String) session.getAttribute("filterMovieGenre");
            int page = (Integer) session.getAttribute("page");

            if (watchlistSvc.checkWatchListByUsernameAndId(username, id)) {
                model.addAttribute("firstFilter", firstFilter);
                model.addAttribute("filterMovieGenre", filterMovieGenre);
                model.addAttribute("page", page);
                return "addedPopularWatchlist";
            }

            return "Error";

        } catch (Exception e) {

            String username = (String) session.getAttribute("username");
            // Get filter and genre
            String firstFilter = (String) session.getAttribute("firstFilter");
            String filterMovieGenre = (String) session.getAttribute("filterMovieGenre");
            int page = (Integer) session.getAttribute("page");

            watchlistSvc.searchMovie(id, username);

            model.addAttribute("firstFilter", firstFilter);
            model.addAttribute("filterMovieGenre", filterMovieGenre);
            model.addAttribute("page", page);
            return "successfulPopularWatchList";
        }

    }

    @PostMapping("/upcoming/{id}")
    public String addUpcomingWatchlist(@PathVariable String id, HttpSession session, Model model) throws Exception {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {

            String username = (String) session.getAttribute("username");
            String filterMovieGenre = (String) session.getAttribute("filterMovieGenre");
            int page = (Integer) session.getAttribute("page");

            if (watchlistSvc.checkWatchListByUsernameAndId(username, id)) {
                model.addAttribute("filterGenre", filterMovieGenre);
                model.addAttribute("page", page);
                return "addedUpcomingWatchlist";
            }

            return "Error";

        } catch (Exception e) {
            String username = (String) session.getAttribute("username");
            String filterMovieGenre = (String) session.getAttribute("filterMovieGenre");
            int page = (Integer) session.getAttribute("page");

            watchlistSvc.searchMovie(id, username);

            model.addAttribute("filterGenre", filterMovieGenre);
            model.addAttribute("page", page);

            return "successfulUpcomingWatchlist";
        }

    }

    @GetMapping("/view")
    public String viewWatchlist(Model model, HttpSession session) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {

            // Get the username from session
            String username = (String) session.getAttribute("username");

            // Get all the watchlist stored in redis based on the username (which is unique)
            List<watchList> watchList1 = watchlistSvc.retrieveWatchList(username);

            // in order not to make the watchlist keep changing
            List<watchList> sortedWatchList = watchList1.stream()
                    .sorted(Comparator.comparing(watchList::getId))
                    .collect(Collectors.toList());

            if (sortedWatchList.size() == 0) {
                model.addAttribute("noWatchList", true);
                return "watchList";
            } else {
                model.addAttribute("watchLists", sortedWatchList);
                // Add a new notes object to watchlist
                model.addAttribute("notes", new Notes());
                return "watchList";
            }

        } catch (Exception e) {

            e.printStackTrace();
            return "noWatchlist";
        }

    }

    @PostMapping("/remove/{id}")
    public String removeWatchList(@PathVariable String id, Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");

        watchlistSvc.removeWatchList(username, id);

        return "redirect:/watchlist/view";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid @ModelAttribute("notes") Notes notes, BindingResult binding, @PathVariable String id,
            Model model, HttpSession session) throws Exception {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        if (binding.hasErrors()) {

            String username = (String) session.getAttribute("username");

            List<watchList> watchList = watchlistSvc.retrieveWatchList(username);
            model.addAttribute("notes", notes);
            model.addAttribute("watchLists", watchList);
            // model.addAttribute("openModalId", id);

            return "watchList";
        }

        String watchStatus = notes.getWatchStatus();
        // System.out.println(watchStatus);
        String username = (String) session.getAttribute("username");
        // System.out.println(username);
        // System.out.println(id);
        int rating = notes.getRatings();
        watchlistSvc.updateWatchList(username, id, watchStatus, rating);

        return "successfulUpdate";

    }

    @GetMapping("/filterMovies")
    public String filterWatchlist(@RequestParam MultiValueMap<String, String> form, HttpSession session, Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {
            String filter = form.getFirst("filterMovie");
            // Get the username from session
            String username = (String) session.getAttribute("username");

            // Get all the watchlist stored in redis based on the username (which is unique)
            List<watchList> watchList1 = watchlistSvc.retrieveWatchList(username);

            if (filter.equals("rating")) {
                // in order not to make the watchlist keep changing
                List<watchList> sortedWatchList = watchList1.stream()
                        .sorted(Comparator.comparing(watchList::getRating).reversed())
                        .collect(Collectors.toList());

                if (sortedWatchList.size() == 0) {
                    model.addAttribute("noWatchList", true);
                    return "watchList";
                } else {
                   
                    model.addAttribute("watchLists", sortedWatchList);
                    // Add a new notes object to watchlist
                    model.addAttribute("notes", new Notes());
                    return "watchList";
                }
            } else if(filter.equals("dateDesc")){
                List<watchList> sortedWatchList = watchList1.stream()
                        .sorted(Comparator.comparing(watchList::getDate).reversed())
                        .collect(Collectors.toList());

                if (sortedWatchList.size() == 0) {
                    model.addAttribute("noWatchList", true);
                    return "watchList";
                } else {
                  
                    model.addAttribute("watchLists", sortedWatchList);
                    // Add a new notes object to watchlist
                    model.addAttribute("notes", new Notes());
                    return "watchList";
                }
            } else if(filter.equals("Watched")){
                List<watchList> sortedWatchlist = watchList1.stream()
                    .filter(p -> p.getWatchStatus().equals("Watched"))
                    .collect(Collectors.toList());

                    if (sortedWatchlist.size() == 0) {
                        model.addAttribute("watchedStatus", true);
                        return "watchList";
                    } else {
                        session.setAttribute("filter", sortedWatchlist);
                        model.addAttribute("watchLists", sortedWatchlist);
                        // Add a new notes object to watchlist
                        model.addAttribute("notes", new Notes());
                        return "watchList";
                    }
            } else if(filter.equals("Unwatched")){
                List<watchList> sortedWatchList = watchList1.stream()
                    .filter(p -> p.getWatchStatus().equals("Unwatched"))
                    .collect(Collectors.toList());

                    if (sortedWatchList.size() == 0) {
                        model.addAttribute("watchedStatus2", true);
                        return "watchList";
                    } else {
                        session.setAttribute("filter", sortedWatchList);
                        model.addAttribute("watchLists", sortedWatchList);
                        // Add a new notes object to watchlist
                        model.addAttribute("notes", new Notes());
                        return "watchList";
                    }
                } else if(filter.equals("In Progress")){
                    List<watchList> sortedWatchList = watchList1.stream()
                        .filter(p -> p.getWatchStatus().equals("In Progress"))
                        .collect(Collectors.toList());
    
                        if (sortedWatchList.size() == 0) {
                            model.addAttribute("watchedStatus3", true);
                            return "watchList";
                        } else {
                            session.setAttribute("filter", sortedWatchList);
                            model.addAttribute("watchLists", sortedWatchList);
                            // Add a new notes object to watchlist
                            model.addAttribute("notes", new Notes());
                            return "watchList";
                        }
                    }

            return "Error";

        } catch (Exception e) {

            e.printStackTrace();
            return "noWatchlist";
        }

    }

    @GetMapping("/removeAll")
    public String removeAll(HttpSession session, Model model){

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        String username = (String) session.getAttribute("username");

        List<watchList> filterList = (List<watchList>) session.getAttribute("filter");

        if(filterList==null || filterList.size()==0){
            System.out.println("Non filter");
            watchlistSvc.removeAll(username);
            model.addAttribute("successful", true);
            return "deleteAllWatchlist";

        } else{
            for(watchList list: filterList){
                System.out.println("filter");
                System.out.println(filterList.size());
                watchlistSvc.removeWatchList(username, list.getId());
            }
            List<watchList> watchList = new ArrayList<>();
            session.setAttribute("filter", watchList);
            return "deleteAllWatchlist";
        }

        
       
    }   

}
