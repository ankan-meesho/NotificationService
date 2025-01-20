package com.example.demo.smsService.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Bean;

import java.time.Instant;

//phone_number
//        message
//status
//        failure_code
//failure_comments
//        created_at
//updated_at


@Entity
@Table(name="sms_requests")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class smsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String phone_number;
    private String message;
    private String status;
    private String faliure_code;
    private String faliure_comment;
    @CreationTimestamp
    private Instant created_at;
    @UpdateTimestamp
    private Instant updated_at;
}
