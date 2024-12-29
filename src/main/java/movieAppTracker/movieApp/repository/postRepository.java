package movieAppTracker.movieApp.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class postRepository {
    
    @Autowired
    @Qualifier("redis-object")
    private RedisTemplate<String,Object> redisTemplate;

    //1. Add movie --> to render page when pressed to reviews 
    public void addMovie(String username,String id, JsonObject object){
        
        HashOperations<String,String,Object> hashOps = redisTemplate.opsForHash();

        Map<String,Object> map = new HashMap<>();
        map.put(id,object.toString());

        hashOps.putAll("movie", map);

    }

    //2. Add comments together with username 
    public void addComments(String username, String id, JsonObject object){

        ListOperations<String,Object> listOps = redisTemplate.opsForList();

        listOps.rightPush("comments " + id ,object.toString());

    }

    //3. Store for RestController 
    public void addJsonComments(JsonObject object){
        
        ListOperations<String,Object> listOps = redisTemplate.opsForList();

        listOps.rightPush("comments",object.toString());

        // Output: ["JsonObject1" , "JsonObject2"]
        
    }

    // Retreive all in
    public List<Object> retrieveJsonComments(){

        ListOperations<String,Object> listOps = redisTemplate.opsForList();

        List<Object> commentsObject= listOps.range("comments",0,-1);

        return commentsObject;
    }

    //Retreive the comments based on movie ID 
    public List<Object> retreiveComments(String id){
        
        ListOperations<String,Object> listOps = redisTemplate.opsForList();

        List<Object> commentsObject= listOps.range("comments " + id,0,-1);

        return commentsObject;
    }



    //Retrieve movie
    public Optional<Map<String,Object>> retreiveMovie(){

        HashOperations<String,String,Object> hashOps = redisTemplate.opsForHash();
        Map<String, Object> mapRetreive = hashOps.entries("movie");

        if(mapRetreive.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(mapRetreive);


        
    }
}
