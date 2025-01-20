package com.example.demo.blacklistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class blacklistService {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String savePhone(String phone) {
        redisTemplate.opsForValue().set(phone,phone);
        return "SuccessFully Blacklisted";
    }

    public List<String> findAll() {
        Set<String> keys = redisTemplate.keys("*");
        List<String>phoneNumbers = new ArrayList<>();
        for (String key : keys) {
            String phoneNumber = redisTemplate.opsForValue().get(key);
            phoneNumbers.add(phoneNumber);
        }
        return phoneNumbers;
    }


    public void delete(String phone) {
        if(redisTemplate.opsForValue().get(phone)==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Phone Not Found");
        }
            redisTemplate.delete(phone);
    }


}
