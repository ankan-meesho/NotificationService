package com.example.demo.blacklistService;

import com.example.demo.smsService.controller.smsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class blacklistService implements blacklistServiceInt {

    Logger logger= LoggerFactory.getLogger(blacklistService.class);


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean savePhone(String phone) {
        try{
            redisTemplate.opsForValue().set(phone,phone);
            logger.info("Number is Successfully blacklisted");
            return true;
        }
        catch (Exception e){
            logger.error("Error saving the number to REDIS "+e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> findAll() {
        try{
            Set<String> keys = redisTemplate.keys("*");
            List<String>phoneNumbers = new ArrayList<>();
            for (String key : keys) {
                String phoneNumber = redisTemplate.opsForValue().get(key);
                phoneNumbers.add(phoneNumber);
            }
            logger.info("Numbers is Successfully blacklisted");
            return phoneNumbers;
        }
        catch (Exception e){
            logger.error("Error retrieving Phone Number "+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }

    }


    @Override
    public void delete(String phone){
        if(redisTemplate.opsForValue().get(phone)==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Phone Not Found");
        }
        try{
            redisTemplate.delete(phone);
        }
        catch (Exception e){
            logger.error("Error deleting Phone "+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }


}
