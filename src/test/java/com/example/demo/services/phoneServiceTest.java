package com.example.demo.services;

import com.example.demo.smsService.entity.phoneEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.smsService.services.serviceInterface.phoneServicesInt;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class phoneServiceTest {
    @Autowired
    private phoneServicesInt phoneServicesInt;


    @ParameterizedTest
    @CsvSource({"1234423424234,testing"})
    void createElasticsearchAndDeleteTest(String phoneNumber, String test) {
        phoneEntity phone = new phoneEntity();
        phone.setPhoneNumber(phoneNumber);
        phone.setMessage(test);
        phoneEntity elasticResult = phoneServicesInt.create(phone);
        assertNotNull(elasticResult);
        phoneEntity Newphone = phoneServicesInt.getphone(elasticResult.getId());
        assertNotNull(Newphone);
        assertTrue(phoneServicesInt.delete(Newphone));
    }

    @ParameterizedTest
    @CsvSource({"test"})
    void getAllPhoneContainingMessageTest(String message) throws IOException {
        assertNotNull(phoneServicesInt.getAllPhoneContainingMessage(message,1,2));
    }

    @ParameterizedTest
    @CsvSource({"No such message is present"})
    void getAllPhoneContainingMessageWrongTest(String message) throws IOException {
        assertNull(phoneServicesInt.getAllPhoneContainingMessage(message,1,2));
    }

    @ParameterizedTest
    @CsvSource({"7001037505,2025-01-20T06:45:27.099Z,2025-01-25T06:45:27.099Z"})
    void getAllPhoneMessageTest(String message, String startDate, String endDate) throws IOException {
        assertNotNull(phoneServicesInt.getAllPhoneMessage(message, startDate, endDate, 1, 2));
    }

    @ParameterizedTest
    @CsvSource({"70010375051,2025-01-20T06:45:27.099Z,2025-01-25T06:45:27.099Z"})
    void getAllPhoneMessageWrongTest(String phone, String startDate, String endDate) {
        assertThrows(ResponseStatusException.class, () -> phoneServicesInt.getAllPhoneMessage(phone, startDate, endDate, 1, 2));
    }
}
