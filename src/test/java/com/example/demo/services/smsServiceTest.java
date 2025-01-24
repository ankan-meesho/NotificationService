package com.example.demo.services;

import com.example.demo.smsService.dto.smsDTO;
import com.example.demo.smsService.services.serviceInterface.smsServicesInt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class smsServiceTest {

    @Autowired
    private smsServicesInt smsServiceInt;


    @ParameterizedTest
    @CsvSource({"1","2","3"})
    void getDetailsByIdTestWrong(String id){
        assertNull(smsServiceInt.getDetailsById(id));
    }

    @ParameterizedTest
    @CsvSource({"3f3390a0-6eb1-4ac3-befe-8bc4618033f5","c06cf9d4-6666-4966-b3fd-9d6a16e15ce8"})
    void getDetailsByIdTestCorrect(String id){
        assertNotNull(smsServiceInt.getDetailsById(id));
    }

    @ParameterizedTest
    @CsvSource({"7001037505,Hello Ankan Welcome to Meesho","1234567890,Meesho is for everyone"})
    void sendSmsTestCorrect(String phoneNumber, String message){
        smsDTO smsDTO = new smsDTO();
        smsDTO.setPhone_number(phoneNumber);
        smsDTO.setMessage(message);
        assertNotNull(smsServiceInt.sendSms(smsDTO));
        assertDoesNotThrow(() -> smsServiceInt.sendSms(smsDTO));
    }

    @ParameterizedTest
    @CsvSource({"9087654321,Hey Number is Blacklisted"})
    void sendSmsTestBlacklist(String phoneNumber, String message){
        smsDTO smsDTO = new smsDTO();
        smsDTO.setPhone_number(phoneNumber);
        smsDTO.setMessage(message);
        assertThrows(ResponseStatusException.class, () -> smsServiceInt.sendSms(smsDTO));
//        assertNull(smsServiceInt.sendSms(smsDTO));
    }

    @ParameterizedTest
    @CsvSource({"087654321,Hey Number is not correct","1234567890,",",Hey there is no phone number"})
    void sendSmsTestWrongInput(String phoneNumber, String message){
        smsDTO smsDTO = new smsDTO();
        smsDTO.setPhone_number(phoneNumber);
        smsDTO.setMessage(message);
        assertThrows(ResponseStatusException.class, () -> smsServiceInt.sendSms(smsDTO));
//        assertNull(smsServiceInt.sendSms(smsDTO));
    }

}
