package movieAppTracker.movieApp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieAppTracker.movieApp.models.userModel.User;
import movieAppTracker.movieApp.models.userModel.UserPassword;
import movieAppTracker.movieApp.repository.userRepository;

@Service
public class userService {
    
    @Autowired
    private userRepository userRepo;

    //Only 1 login per session, store the session for username
    public void storeSessionUsername(String username){
        
        userRepo.storeSession(username);
         
    }

    public String checkSession(String username){
        return userRepo.getSession(username);
    }

    public void removeSession(String username){
        userRepo.removeSession(username);
    }

    //Store user details
    public void storeUserDetails(User user){
        
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();

        userRepo.storeUserDetails(username,password,email);
    }


    //Retrieve user details for login
    public String authenthicate(User user){

        String username = user.getUsername();
        //String password = user.getPassword();

        return userRepo.retreiveUserDetails(username);
    }

    //Update user details 
    public void updateUserDetails(UserPassword user){

        String email= user.getEmail();
        String password = user.getPassword();

        userRepo.updateUserDetails(email, password);
    }

    //Retreive whether there is a username for forgetpassword, if not ask them to sign up for an account 
    public String authenthicateForgetPassword(UserPassword newPassword){
        
        String email = newPassword.getEmail();

        return userRepo.retrieveUsername(email);
    }

    //Retreive the stored username 
    public Boolean checkUsername(String username) throws Exception{

        List<String> checkUsername = userRepo.retreiveStoredUsername();

        if(checkUsername == null || checkUsername.isEmpty()){
            return true;
        }

        for(String check: checkUsername){
            if(username.equals(check)){
                throw new Exception("Same username!!");
            }
        }

        return false;
    }

}
