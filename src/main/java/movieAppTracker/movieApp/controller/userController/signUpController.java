package movieAppTracker.movieApp.controller.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import movieAppTracker.movieApp.models.userModel.User;
import movieAppTracker.movieApp.service.userService;

@Controller
@RequestMapping("/signup")
public class signUpController {
    
    @Autowired
    private userService userSvc;


    @GetMapping("")
    public String signUp(Model model){
        User user = new User();
        
        model.addAttribute("user", user);
        return "signupPage";
    }

    @PostMapping("/userSignUp")
    public String userSignUp(@Valid @ModelAttribute("user") User user, BindingResult binding, Model model){

        if(binding.hasErrors()){
            return "signupPage";
        }

        try {

            //If list gt nothing -> return true, store the username, that is the first thing (for inital signUp)
            if(userSvc.checkUsername(user.getUsername())){
                userSvc.storeUserDetails(user);
                return "signUpSuccess";
            } 

            //Else can continue to store 
            userSvc.storeUserDetails(user);

            return "signUpSuccess";

        } catch (Exception e) {
            //Same username 
            model.addAttribute("sameUsername", true);
            return "signupPage";
        }
        
    }
}
