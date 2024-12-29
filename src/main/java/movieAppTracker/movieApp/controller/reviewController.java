package movieAppTracker.movieApp.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import movieAppTracker.movieApp.models.Comments;
import movieAppTracker.movieApp.models.Notes;
import movieAppTracker.movieApp.models.watchList;
import movieAppTracker.movieApp.models.movieModel.Movie;
import movieAppTracker.movieApp.service.postService;

@Controller
@RequestMapping("/post")
public class reviewController {

    @Autowired
    private postService postSvc;

    @PostMapping("/{id}")
    public String postComments(@RequestBody MultiValueMap<String, String> form, @PathVariable String id, Model model,
            HttpSession session)
            throws Exception {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {

            String comments = form.getFirst("comments");
            int rating = Integer.parseInt(form.getFirst("rating"));
            String username = (String) session.getAttribute("username");
            postSvc.storeComments(id, username, comments, rating);

            return "successfulPost";

        } catch (Exception e) {
            e.printStackTrace();

            return "Error";
        }

    }

    @GetMapping("/viewReviews")
    public String viewComments(HttpSession session, Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        try {

            List<Movie> commentList = postSvc.retreiveMovie();
            List<Movie> sortedCommentList = commentList.stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(Collectors.toList());

            if (commentList.size() == 0) {
                model.addAttribute("noComments", true);
                return "reviews";
            } else {
                model.addAttribute("commentLists", sortedCommentList);
                return "reviews";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "noReviews";
        }
    }

    @GetMapping("/allReviews/{id}")
    public String allReviews(@PathVariable String id, Model model) throws Exception {

        // Retreive the stored reviews
        List<Comments> commentList = postSvc.retreiveComments(id);

        model.addAttribute("comments", commentList);

        return "allReviews";
    }
}
