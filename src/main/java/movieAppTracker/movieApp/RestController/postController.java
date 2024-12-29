package movieAppTracker.movieApp.RestController;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonObject;
import movieAppTracker.movieApp.service.postService;

@RestController
@RequestMapping("/api")
public class postController {
    
    @Autowired
    private postService postSvc;

    @GetMapping(path="/viewAllComments", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllComments(){

        JsonObject object = postSvc.retreiveAllComments();

        return ResponseEntity.ok().body(object.toString());
    } 
}
