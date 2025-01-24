package com.example.demo.blacklistService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/blacklist")
public class blacklistController {

    Logger logger = LoggerFactory.getLogger(blacklistController.class);

    private final blacklistService blacklistService;
    public blacklistController(blacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> addBlacklist(@RequestBody requestType phoneNumbers) {
        try{
            String regexStr = "^[1-9][0-9]{9}$";
            for(String phoneNumber: phoneNumbers.getBlacklistPhoneNumber()) {
                if(Pattern.matches(regexStr, phoneNumber)) {
                    if(!blacklistService.savePhone(phoneNumber)){
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error saving phone number");
                    }
                }
                else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid phone number");
                }
            }
            Map<String, String> response=new HashMap<>();
            response.put("data","Successfully added blacklist");
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<Map<String,List<String>>> getBlacklist() {
        try{
            List<String>phoneNumbers=blacklistService.findAll();
            Map<String, List<String>> response=new HashMap<>();
            response.put("phone_Numbers",phoneNumbers);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error Getting the phone numbers");
        }

    }


    @DeleteMapping
    public ResponseEntity<Map<String,String>> deleteBlacklist(@RequestBody requestType phoneNumbers) {
        try{
            String regexStr = "^[1-9][0-9]{9}$";
            for(String phoneNumber: phoneNumbers.getBlacklistPhoneNumber()) {
                if(Pattern.matches(regexStr, phoneNumber)) {
                    blacklistService.delete(phoneNumber);
                }
                else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid phone number");
                }
            }
            Map<String,String> response = new HashMap<>();
            response.put("data", "successfully whitelisted");
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete blacklisted Numbers");
        }

    }

}
