package com.example.demo.blacklistService;

import java.util.List;

public interface blacklistServiceInt {
    boolean savePhone(String phone);

    List<String> findAll();

    void delete(String phone);
}
