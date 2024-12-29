package movieAppTracker.movieApp.controller.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import movieAppTracker.movieApp.models.userModel.User;
import movieAppTracker.movieApp.service.userService;

@Controller
@RequestMapping("/")
public class loginController {

    @Autowired
    private userService userSvc;

    @GetMapping("")
    public String landingPage(Model model) {

        User user = new User();
        model.addAttribute("user", user);

        return "landingPage";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") User user, BindingResult binding, HttpSession session,
            Model model) {

        if (binding.hasErrors()) {
            // System.out.println(binding.getAllErrors());
            return "landingPage";
        }

        // Set the countLogin to 0
        Integer countLogin = (Integer) session.getAttribute("countLogin");
        if (countLogin == null) {
            countLogin = 0;
        }

        // System.out.println(countLogin);

        String password = user.getPassword();
        String retreivePassword = userSvc.authenthicate(user);
        // System.out.println(retreivePassword);
        // System.out.println(password);

        if (retreivePassword.isEmpty()) {
            model.addAttribute("noAccount", true);
            return "landingPage";
        }

        if (!retreivePassword.equals(password)) {
            countLogin++;
            session.setAttribute("countLogin", countLogin);
            // System.out.println(countLogin);
            if (countLogin <= 5) {
                model.addAttribute("wrongPassword", true);
                return "landingPage";
            }

            session.invalidate();
            return "unauthorised";
        }

        // if nothing is stored thn store it
        if (userSvc.checkSession(user.getUsername()) == null || userSvc.checkSession(user.getUsername()).isEmpty()) {
            userSvc.storeSessionUsername(user.getUsername());
        } else {
            return "loggedIn";
        }

        // After succesfull Login
        session.setAttribute("isLoggedIn", true);
        session.setAttribute("countLogin", 0);
        session.setAttribute("username", user.getUsername());

        return "redirect:/protected/dashboard";

    }

}
