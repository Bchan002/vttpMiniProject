package movieAppTracker.movieApp.repository;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class userRepository {
    
    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> redisTemplate;

    //Abit redundant but to prevent simultaneous login within a short period of time, set to 2 mins duration
    public void storeSession(String username){
        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        valueOps.set("session: " + username, username);

        Duration duration = Duration.ofMinutes(3);
        redisTemplate.expire("session: " + username, duration);

    }

    public String getSession(String username){
        return redisTemplate.opsForValue().get("session: " + username);
    }

    public void removeSession(String username){
        redisTemplate.delete("session: " + username);
    }

    public void storeUserDetails(String username, String password,String email){

        //Hset username email 
        //Hget username email ---> password
        HashOperations<String,String,String> hashOps = redisTemplate.opsForHash();

        Map<String,String> map = new HashMap<>();
        map.put(email,password);

        hashOps.putAll("username: " + username,map);

        //set email --> username
        //for sign up 
        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        valueOps.set(email,"username: " + username );

        //This is to check for Uniquess of username 
        ListOperations<String,String> listOps = redisTemplate.opsForList();
        listOps.rightPush("storedUsername: ", username);
       
    }

    public String retreiveUserDetails(String username){

        HashOperations<String, String,String> hashOps = redisTemplate.opsForHash();

        Map<String,String> map = hashOps.entries("username: " + username);
        String retreivePassword=""; 
        for(String pw: map.keySet()){
            retreivePassword = map.get(pw);
        }

        return retreivePassword;
    }

    public void updateUserDetails(String email, String newPassword){

        //Get the email ---> username 
        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        //Already in "username: + username"
        String username = valueOps.get(email);

        //ERROR HERE
        //Update the new password 
        HashOperations<String,String, String> hashOps = redisTemplate.opsForHash();
        Map<String,String> map = new HashMap<>();
        map.put(email,newPassword);

        hashOps.putAll(username, map);

    }

    public String retrieveUsername(String email){

        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        String username = valueOps.get(email);

        return username;
    }

    //Retreive stored username 
    public List<String> retreiveStoredUsername(){

        ListOperations<String,String> listOps = redisTemplate.opsForList();
        
        List<String> usernameList = listOps.range("storedUsername: ",0,-1);

        return usernameList;
        
       
    }

    
}
