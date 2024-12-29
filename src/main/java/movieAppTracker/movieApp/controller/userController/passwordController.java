package movieAppTracker.movieApp.controller.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import movieAppTracker.movieApp.models.userModel.User;
import movieAppTracker.movieApp.models.userModel.UserPassword;
import movieAppTracker.movieApp.service.userService;

@Controller
@RequestMapping("/forgetPassword")
public class passwordController {
    
    @Autowired
    private userService userSvc;

    @GetMapping("")
    public String forgetPassword(Model model){
        
        model.addAttribute("userPassword", new UserPassword());
        return "forgetPassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid @ModelAttribute("userPassword") UserPassword newPassword, BindingResult binding, Model model){
        
        if(binding.hasErrors()){
            return "/forgetPassword";
        }

        if(userSvc.authenthicateForgetPassword(newPassword)==null){
            model.addAttribute("noAccount", true);

            return "forgetPassword";
        }

        userSvc.updateUserDetails(newPassword);

        return "successfulChangePassword";
    }
}
