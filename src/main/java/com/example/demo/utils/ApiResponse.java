package com.example.demo.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiResponse {
    private String id;
    private String message;
    private String phone_number;

}
