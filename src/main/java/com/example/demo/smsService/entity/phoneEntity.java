package com.example.demo.smsService.entity;


import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Document(indexName = "phone_index")
@Data
public class phoneEntity {
    private String id;
    @Field(type = FieldType.Text,name = "phoneNumber")
    private String phoneNumber;
    @Field(type = FieldType.Text,name = "message")
    private String message;
    @Field(type = FieldType.Date,format = DateFormat.date_time,name = "sentDate")
    private Instant sentDate;
}
