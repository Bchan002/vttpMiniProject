package movieAppTracker.movieApp.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class guessMovieRepository {
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void storeGuessMovieTitle(int pageNum,JsonObject object){

        //Store as List ?
        // ListOperations<String,Object> listOps = redisTemplate.opsForList();
        
        // listOps.rightPush("guessMovie", object.toString());

        //Store as Map? 
        HashOperations<String,String,Object> hashOps = redisTemplate.opsForHash();
        
        Map<String,Object> map = new HashMap<>();
        map.put(String.valueOf(pageNum), object.toString());

        hashOps.putAll("guessMovie",map);
    }


    //Retreive the stored movie titles; 
    public Optional<Map<String,Object>> retreiveMovie(){

        HashOperations<String,String,Object> hashOps = redisTemplate.opsForHash();
        Map<String, Object> mapRetreive = hashOps.entries("guessMovie");

        if(mapRetreive.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(mapRetreive);
        
    }
     
}
