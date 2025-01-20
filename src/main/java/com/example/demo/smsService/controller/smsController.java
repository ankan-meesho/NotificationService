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
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/sms")
public class smsController {

    Logger logger= LoggerFactory.getLogger(smsController.class);
    public com.example.demo.smsService.services.smsServices smsServices;
    @Autowired
    private final phoneServices phoneServices;

    public smsController(smsServices smsServices, KafkaServices kafkaServices, smsRepository smsRepository, com.example.demo.smsService.services.phoneServices phoneServices) {
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
        if(Objects.equals(response.getStatus(), "404")){
            JSONObject obj=new JSONObject();
            obj.put("code",response.getFaliure_code());
            obj.put("comment",response.getFaliure_comment());
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
    public List<phoneEntity> find(@RequestBody requestType message) {
        System.out.printf("message: %s\n", message.getMessage());
        return phoneServices.getAllPhoneContainingMessage(message.getMessage());
    }

    @PostMapping("/find_all_sms_from_phone")
    public List<phoneEntity> find_phone(@RequestBody requestType message) {
        int page=1;
        int size=2;
        return phoneServices.getAllPhoneMessage(message.getPhone(), message.getStartime(), message.getEndtime(),page,size);
    }
}
