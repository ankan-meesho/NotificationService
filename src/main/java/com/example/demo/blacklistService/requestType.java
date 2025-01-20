package com.example.demo.blacklistService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class requestType {
    List<String>blacklistPhoneNumber;
}
