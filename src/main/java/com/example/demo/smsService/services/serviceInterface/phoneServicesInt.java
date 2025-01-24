package com.example.demo.smsService.services.serviceInterface;

import com.example.demo.smsService.entity.phoneEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface phoneServicesInt {
    phoneEntity create(phoneEntity phoneEntity);
    boolean delete(phoneEntity phoneEntity);

    phoneEntity getphone(String id);

    List<Map<String, Object>> getAllPhoneContainingMessage(String message,int pageNumber,int pageSize) throws IOException;

    List<Map<String, Object>> getAllPhoneMessage(String phone, String startTime, String endTime, int page, int size) throws IOException;
}
