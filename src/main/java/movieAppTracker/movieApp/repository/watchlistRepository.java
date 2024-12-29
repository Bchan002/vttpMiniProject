package movieAppTracker.movieApp.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class watchlistRepository {
    
    @Autowired
    @Qualifier("redis-object")
    private RedisTemplate<String,Object> redisTemplate;

    //Hset username id
    public void addWatchList(String username, String id, JsonObject object){

        HashOperations<String,String,Object> hashOps = redisTemplate.opsForHash();

        Map<String,Object> map = new HashMap<>();
        map.put(id,object.toString());

        // Can store as username as it is unique, each username has their own unique watchlist
        hashOps.putAll(username,map);
    }


    //Retrieve WatchList based on username
    public Optional<Map<String,Object>> retreiveWatchList(String username){

        HashOperations<String,String,Object> hashOps = redisTemplate.opsForHash();
        Map<String, Object> mapRetreive = hashOps.entries(username);

        if(mapRetreive.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(mapRetreive);
    }

    public void removeWatchList(String username, String id){

        HashOperations<String,String,Object> hashOps = redisTemplate.opsForHash();
        hashOps.delete(username, id);

    }

    //Delete key 
    public void removeAllWatchList(String username){
        
        redisTemplate.delete(username);
    }

    public String retrieveWatchLisByUsernameAndId(String username, String id) throws Exception{
        
       Object movieDetails = redisTemplate.opsForHash().get(username, id);

       if(movieDetails==null){
        throw new Exception("movieDetails is null");
       }

        return movieDetails.toString();
    }

}
