package com.example.demo.blacklistService;


import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blacklist")
public class blacklistController {
    private final blacklistService blacklistService;
    public blacklistController(blacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @PostMapping
    public String addBlacklist(@RequestBody requestType phoneNumbers) {
        System.out.println(phoneNumbers.getBlacklistPhoneNumber());
        for(String phoneNumber: phoneNumbers.getBlacklistPhoneNumber()) {
            blacklistService.savePhone(phoneNumber);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", "successfully blacklisted");
        return jsonObject.toString();
    }

    @GetMapping
    public String getBlacklist() {

        List<String>phoneNumbers=blacklistService.findAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", phoneNumbers);
        return jsonObject.toString();
    }


    @DeleteMapping
    public HashMap<String,String> deleteBlacklist(@RequestBody requestType phoneNumbers) {
        for(String phoneNumber: phoneNumbers.getBlacklistPhoneNumber()) {
            blacklistService.delete(phoneNumber);
        }
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("data", "successfully whitelisted");
        return hashMap;
    }

}
