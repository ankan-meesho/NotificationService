package com.example.demo.smsService.services.serviceInterface;

import com.example.demo.smsService.dto.smsDTO;
import com.example.demo.utils.ApiResponse;

public interface smsServicesInt {
    ApiResponse sendSms(smsDTO smsDTO);

    smsDTO getDetailsById(String id);
}
