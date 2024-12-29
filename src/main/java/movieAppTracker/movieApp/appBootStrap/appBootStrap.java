package movieAppTracker.movieApp.appBootStrap;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import movieAppTracker.movieApp.service.guessMovieService;

@Component
public class appBootStrap implements CommandLineRunner{
    
    @Autowired
    private guessMovieService guessMovieSvc;


    @Override
    public void run(String... args)  throws Exception{
        
       guessMovieSvc.storePopularMovie();
        
    }
    
}
