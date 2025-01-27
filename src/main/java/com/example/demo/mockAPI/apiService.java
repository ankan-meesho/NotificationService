package com.example.demo.mockAPI;

import com.example.demo.smsService.services.KafkaServices;
import com.example.demo.utils.AppConstant;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class apiService {
    Logger logger= LoggerFactory.getLogger(KafkaServices.class);

    Random rand = new Random();
    public boolean start(String message,String phone,String header) {
//        TimeUnit.SECONDS.sleep(5);
        /**
         * MOCK API RANDOMLY FAILS
         **/
        if(Objects.equals(header, AppConstant.MOCK_API_KEY)){
            if(rand.nextInt(20)%5==0){
                System.out.println("Failed");
                logger.warn("Failed!! Retry in progress");
                return false;
            }
            else {
                logger.info("Successfully called API");
                return true;
            }
        }
        else{
            logger.error("API ERROR");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"API Key is not valid");
        }
    }
}
