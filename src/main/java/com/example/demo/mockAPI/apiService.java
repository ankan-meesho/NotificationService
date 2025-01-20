package com.example.demo.mockAPI;

import com.example.demo.utils.AppConstant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class apiService {
    Random rand = new Random();
    public boolean start(String message,String phone,String header) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        if(header == AppConstant.MOCK_API_KEY){
            if(rand.nextInt(20)%5==0){
                System.out.println("Failed");
                return false;
            }
            else {
                System.out.println("Success");
                return true;
            }
        }
        else{
            System.out.println("Failed");
            return false;
        }
    }
}
