package com.example.demo.smsService.controller;

import com.example.demo.mockAPI.apiService;
import com.example.demo.utils.ApiResponse;
import com.example.demo.smsService.entity.phoneEntity;
import com.example.demo.smsService.repository.smsRepository;
import com.example.demo.utils.requestType;
import com.example.demo.smsService.services.KafkaServices;
import com.example.demo.smsService.services.phoneServices;
import com.example.demo.smsService.services.smsServices;
import com.example.demo.smsService.dto.smsDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/sms")
public class smsController {

    Logger logger= LoggerFactory.getLogger(smsController.class);
    @Autowired
    public com.example.demo.smsService.services.smsServices smsServices;
    @Autowired
    private final phoneServices phoneServices;

    public smsController(smsServices smsServices,phoneServices phoneServices) {
        this.smsServices = smsServices;
        this.phoneServices = phoneServices;
    }


    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendSms(@RequestBody smsDTO smsDTO) {
        try
        {
            ApiResponse response = smsServices.sendSms(smsDTO);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            System.out.printf("SMS Error: %s",e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{request_id}")
    public ResponseEntity<String> getSms(@PathVariable String request_id) {
        smsDTO response= smsServices.getDetailsById(request_id);
        if(response==null){
            JSONObject obj=new JSONObject();
            obj.put("code","INVALID_REQUEST");
            obj.put("comment","request_id not found");
            return ResponseEntity.status(404).body(obj.toString());
        }
        JSONObject obj=new JSONObject();
        obj.put("id",response.getId());
        obj.put("phone_number",response.getPhone_number());
        obj.put("message",response.getMessage());
        return ResponseEntity.ok(obj.toString());
    }

//    Just for debugging
    @GetMapping("/els/{id}")
    public phoneEntity test(@PathVariable String id) {
        return phoneServices.getphone(id);
    }

    @PostMapping("/find_from_sms")
    public List<Map<String, Object>> find(@RequestBody requestType message) {
        try{
            return phoneServices.getAllPhoneContainingMessage(message.getMessage(), message.getPageNumber(), message.getPageSize());
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/find_all_sms_from_phone")
    public List<Map<String, Object>> find_phone(@RequestBody requestType message) {
        try{
            return phoneServices.getAllPhoneMessage(message.getPhone(), message.getStartime(), message.getEndtime(), message.getPageNumber(), message.getPageSize());
        }
        catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
