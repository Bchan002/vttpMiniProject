package movieAppTracker.movieApp.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpSession;
import movieAppTracker.movieApp.models.movieModel.Movie;
import movieAppTracker.movieApp.service.guessMovieService;

@Controller
@RequestMapping("/guessMovieGame")
public class guessMovieGameController {

    @Autowired
    private guessMovieService guessMovieSvc;

    @GetMapping("")
    public String difficultyLevel(HttpSession session, Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }
        return "guessMovieDifficulty";
    }

    @GetMapping("/low")
    public String guessGameLow(HttpSession session, Model model) throws Exception {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        // Once start set the no.of tries to 0
        session.setAttribute("tries", 0);

        List<Movie> guessMovie = guessMovieSvc.retreiveMovie();

        List<String> getMovieDetails = guessMovieSvc.getRandomMovieTitlesLow(guessMovie);

        String movieTitle = getMovieDetails.get(0);
        String movieOverview = getMovieDetails.get(1);

        // Rmb: MovieTitle is always uppercase
        session.setAttribute("movieTitle", movieTitle);

        // Check movie title size
        int length = movieTitle.length();

        // Create a display array
        String[] display = new String[length];

        // Create a movieTitle array from movieTitle
        String[] movieTitleArray = movieTitle.split("");

        // Check whether there is any colon in between?
        for (int i = 0; i < display.length; i++) {
            if (movieTitleArray[i].equals(":")) {
                display[i] = ":";
            } else if (movieTitleArray[i].equals(" ")) {
                display[i] = " ";
            }
        }

        // Populate the display with the movieTitle Array randomly
        Random random = new Random();
        int i = 1;

        int clues = 1;
        while (i <= clues) {
            int randomIndex = random.nextInt(0, length);
            if (display[randomIndex] == null || display[randomIndex].isEmpty()) {
                display[randomIndex] = movieTitleArray[randomIndex];
                i++;
            }
        }

        // Create an empty user array based on the size of the movie title
        String[] userArray = new String[length];
        // Check whether any colons or empty spaces for userArray as well
        for (int j = 0; j < display.length; j++) {
            if (movieTitleArray[j].equals(":")) {
                userArray[j] = ":";
            } else if (movieTitleArray[j].equals(" ")) {
                userArray[j] = " ";
            }
        }

        session.setAttribute("display", display);
        session.setAttribute("userArray", userArray);
        session.setAttribute("movieOverview", movieOverview);

        model.addAttribute("display", display);
        model.addAttribute("userArray", userArray);
        model.addAttribute("movieOverview", movieOverview);
        model.addAttribute("totalTries", true);
        model.addAttribute("checkAnswer", true);

        return "guessMovie";
    }

    @GetMapping("/medium")
    public String guessGameMedium(HttpSession session, Model model) throws Exception {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        // Once start set the no.of tries to 0
        session.setAttribute("tries", 0);

        List<Movie> guessMovie = guessMovieSvc.retreiveMovie();

        List<String> getMovieDetails = guessMovieSvc.getRandomMovieTitlesMedium(guessMovie);

        String movieTitle = getMovieDetails.get(0);
        String movieOverview = getMovieDetails.get(1);

        // Rmb: MovieTitle is always uppercase
        session.setAttribute("movieTitle", movieTitle);

        // Check movie title size
        int length = movieTitle.length();

        // Create a display array
        String[] display = new String[length];

        // Create a movieTitle array from movieTitle
        String[] movieTitleArray = movieTitle.split("");

        // Check whether there is any colon in between?
        for (int i = 0; i < display.length; i++) {
            if (movieTitleArray[i].equals(":")) {
                display[i] = ":";
            } else if (movieTitleArray[i].equals(" ")) {
                display[i] = " ";
            }
        }

        // Populate the display with the movieTitle Array randomly
        Random random = new Random();
        int i = 1;

        if (length >5 && length <= 10) {
            int clues = 3;
            while (i <= clues) {
                int randomIndex = random.nextInt(0, length);
                if (display[randomIndex] == null || display[randomIndex].isEmpty()) {
                    display[randomIndex] = movieTitleArray[randomIndex];
                    i++;
                }
            }
        } else {
            int clues = 1;
            while (i <= clues) {
                int randomIndex = random.nextInt(0, length);
                if (display[randomIndex] == null || display[randomIndex].isEmpty()) {
                    display[randomIndex] = movieTitleArray[randomIndex];
                    i++;
                }
            }
        }

        // Create an empty user array based on the size of the movie title
        String[] userArray = new String[length];
        // Check whether any colons or empty spaces for userArray as well
        for (int j = 0; j < display.length; j++) {
            if (movieTitleArray[j].equals(":")) {
                userArray[j] = ":";
            } else if (movieTitleArray[j].equals(" ")) {
                userArray[j] = " ";
            }
        }

        session.setAttribute("display", display);
        session.setAttribute("userArray", userArray);
        session.setAttribute("movieOverview", movieOverview);

        model.addAttribute("display", display);
        model.addAttribute("userArray", userArray);
        model.addAttribute("movieOverview", movieOverview);
        model.addAttribute("totalTries", true);
        model.addAttribute("checkAnswer", true);

        return "guessMovie";
    }

    @GetMapping("/hard")
    public String guessGameHard(HttpSession session, Model model) throws Exception {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        // Once start set the no.of tries to 0
        session.setAttribute("tries", 0);

        List<Movie> guessMovie = guessMovieSvc.retreiveMovie();

        List<String> getMovieDetails = guessMovieSvc.getRandomMovieTitlesHard(guessMovie);

        String movieTitle = getMovieDetails.get(0);
        String movieOverview = getMovieDetails.get(1);

        // Rmb: MovieTitle is always uppercase
        session.setAttribute("movieTitle", movieTitle);

        // Check movie title size
        int length = movieTitle.length();

        // Create a display array
        String[] display = new String[length];

        // Create a movieTitle array from movieTitle
        String[] movieTitleArray = movieTitle.split("");

        // Check whether there is any colon in between?
        for (int i = 0; i < display.length; i++) {
            if (movieTitleArray[i].equals(":")) {
                display[i] = ":";
            } else if (movieTitleArray[i].equals(" ")) {
                display[i] = " ";
            }
        }

        // Populate the display with the movieTitle Array randomly
        Random random = new Random();
        int i = 1;
        if(length>15 && length<=20){
            int clues = 5;
            while (i <= clues) {
                int randomIndex = random.nextInt(0, length);
                if (display[randomIndex] == null || display[randomIndex].isEmpty()) {
                    display[randomIndex] = movieTitleArray[randomIndex];
                    i++;
                }
            }
        } else if(length>10 && length<=15){
            int clues = 4;
            while (i <= clues) {
                int randomIndex = random.nextInt(0, length);
                if (display[randomIndex] == null || display[randomIndex].isEmpty()) {
                    display[randomIndex] = movieTitleArray[randomIndex];
                    i++;
                }
            }
        } else if (length >5 && length <= 10) {
            int clues = 2;
            while (i <= clues) {
                int randomIndex = random.nextInt(0, length);
                if (display[randomIndex] == null || display[randomIndex].isEmpty()) {
                    display[randomIndex] = movieTitleArray[randomIndex];
                    i++;
                }
            }
        }  

        // Create an empty user array based on the size of the movie title
        String[] userArray = new String[length];
        // Check whether any colons or empty spaces for userArray as well
        for (int j = 0; j < display.length; j++) {
            if (movieTitleArray[j].equals(":")) {
                userArray[j] = ":";
            } else if (movieTitleArray[j].equals(" ")) {
                userArray[j] = " ";
            }
        }

        session.setAttribute("display", display);
        session.setAttribute("userArray", userArray);
        session.setAttribute("movieOverview", movieOverview);

        model.addAttribute("display", display);
        model.addAttribute("userArray", userArray);
        model.addAttribute("movieOverview", movieOverview);
        model.addAttribute("totalTries", true);
        model.addAttribute("checkAnswer", true);

        return "guessMovie";
    }

    @PostMapping("/userGuess")
    public String checkUserGuess(@RequestBody MultiValueMap<String, String> form, HttpSession session, Model model) {

        if (session.getAttribute("isLoggedIn") == null || (Boolean) session.getAttribute("isLoggedIn") == false) {
            return "unauthorised";
        }

        int tries = (Integer) session.getAttribute("tries");
        tries += 1;
        session.setAttribute("tries", tries);
        System.out.println(tries);

        String[] userArray = (String[]) session.getAttribute("userArray");

        // 2. this is to check for the wordings
        String userInput = "";
        for (String form2 : form.keySet()) {
            userInput += form.getFirst(form2);
        }

        // Take the movie title from the session and change it to all CAPS for checks
        String movieTitle = (String) session.getAttribute("movieTitle");

        if (tries <= 5) {

            if (guessMovieSvc.checkWords(userInput, movieTitle)) {
                model.addAttribute("win", true);
                // display the movie title
                model.addAttribute("display", movieTitle.split(""));

                return "guessMovie";
            }

            String[] display = (String[]) session.getAttribute("display");
            String movieOverview = (String) session.getAttribute("movieOverview");

            String movieTitle2 = (String) session.getAttribute("movieTitle");
            String[] movieTitle3 = movieTitle2.split("");

            // 1. This is to put back the values when fail
            String[] failedUserInput = new String[userArray.length];

            for (int i = 0; i < failedUserInput.length; i++) {
                failedUserInput[i] = form.getFirst("guess" + i);
                if (failedUserInput[i] != null) {
                    failedUserInput[i] = failedUserInput[i].toUpperCase();
                }

            }

            for (int i = 0; i < failedUserInput.length; i++) {
                // handle null values and convert it to space
                if (failedUserInput[i] == null) {
                    failedUserInput[i] = " ";
                }

                if (failedUserInput[i].equals(movieTitle3[i])) {
                    failedUserInput[i] = movieTitle3[i];
                } else {
                    failedUserInput[i] = "";
                }
            }

            model.addAttribute("display", display);
            model.addAttribute("userArray", failedUserInput);
            model.addAttribute("movieOverview", movieOverview);
            model.addAttribute("tries", (int) session.getAttribute("tries"));
            model.addAttribute("checkAnswer", true);
            return "guessMovie";
        }

        model.addAttribute("display", movieTitle.split(""));
        model.addAttribute("lost", true);
        return "guessMovie";
    }

}
