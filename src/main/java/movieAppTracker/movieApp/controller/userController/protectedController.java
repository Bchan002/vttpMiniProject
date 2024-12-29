package movieAppTracker.movieApp.controller.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import movieAppTracker.movieApp.repository.userRepository;

@Controller
@RequestMapping("/protected")
public class protectedController {
    
    @Autowired
    private userRepository userRepo;


    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model){
        
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String username = (String) session.getAttribute("username");

        if(isLoggedIn==null || isLoggedIn == false){
            return "unauthorised";
        }


        model.addAttribute("username", username);
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        
        userRepo.removeSession((String) session.getAttribute("username"));
        session.invalidate();

        return "redirect:/";
    }


}
