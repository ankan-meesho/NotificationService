package com.example.demo.smsService.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class smsDTO {
    private String id;
    private String phone_number;
    private String message;
    private String status;
    private String faliure_code;
    private String faliure_comment;
    private Instant created_at;
    private Instant updated_at;
}
