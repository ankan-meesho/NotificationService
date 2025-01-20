package com.example.demo.healcheck;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthcheckController {
    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthcheck() {
        JSONObject obj = new JSONObject();
        obj.put("status", "UP");
        obj.put("message", "OK");
        return ResponseEntity.ok(obj.toString());
    }
}
