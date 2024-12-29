package movieAppTracker.movieApp.models.userModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class User {
    
    @NotNull(message="username must not be null")
    @NotBlank(message="username must not be blank")
    @Size(min=5, max=20, message="username must be at least 5 characters")
    private String username;


    @NotNull(message="username must not be null")
    @NotBlank(message="username must not be blank")
    @Size(min=5, max=20, message="password must be at least 5 characters")
    private String password;

    @Email(message="Must be a valid email")
    private String email;


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    



}
