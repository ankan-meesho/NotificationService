package com.example.demo.services;

import com.example.demo.blacklistService.blacklistServiceInt;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class blacklistServiceTest {

    @Autowired
    private blacklistServiceInt blacklistService;


    @Order(1)
    @ParameterizedTest
    @CsvSource({"9999999990","9999991230"})
    public void blacklistNumber(String number) {
        assertTrue(blacklistService.savePhone(number));
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource({"9999999990","9999991230"})
    public void whitelistNumber(String number) {
        assertDoesNotThrow(() -> blacklistService.delete(number));
    }

    @Order(3)
    @ParameterizedTest
    @CsvSource({"9999999990"})
    public void FindNumber(String number){
        assertTrue(blacklistService.savePhone(number));
        List<String> phoneNumbers=blacklistService.findAll();
        assertTrue(phoneNumbers.contains(number));
        assertDoesNotThrow(() -> blacklistService.delete(number));
    }
}
