package com.example.demo.smsService.services;

import com.example.demo.utils.AppConstant;
import com.example.demo.smsService.controller.smsController;
import com.example.demo.smsService.entity.phoneEntity;
import com.example.demo.smsService.entity.smsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class KafkaServices {
    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private final com.example.demo.smsService.repository.smsRepository smsRepository;
    @Autowired
    private final com.example.demo.smsService.services.phoneServices phoneServices;


    Logger logger= LoggerFactory.getLogger(KafkaServices.class);
    @Autowired
    private com.example.demo.mockAPI.apiService apiService;


    public KafkaServices(KafkaTemplate<String, String> kafkaTemplate, com.example.demo.smsService.repository.smsRepository smsRepository, com.example.demo.smsService.services.phoneServices phoneServices) {
        this.kafkaTemplate = kafkaTemplate;
        this.smsRepository = smsRepository;
        this.phoneServices = phoneServices;
    }

    @Async
    public boolean sendMessage(String message) {
        try{
            kafkaTemplate.send(AppConstant.TOPIC_NAME, message);
            logger.info("Message sent successfully to topic: " + AppConstant.TOPIC_NAME + " having request_id : " + message);
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            System.out.println(e);
            return false;
        }

    }



    @KafkaListener(topics = AppConstant.TOPIC_NAME,groupId = AppConstant.GROUP_ID)
    public String receiveMessage(String message) throws InterruptedException {
        System.out.printf("Received Message: %s\n", message);
        message=message.substring(1,message.length()-1);

        smsEntity smsEntity= smsRepository.findById(message).orElse(null);
        if(smsEntity != null) {

                if(apiService.start(smsEntity.getMessage(),smsEntity.getPhone_number(),AppConstant.MOCK_API_KEY)){
                    smsEntity.setStatus("API Called Successfully");
                    smsRepository.save(smsEntity);
                    phoneEntity elasticDoc=new phoneEntity();
                    elasticDoc.setPhoneNumber(smsEntity.getPhone_number());
                    elasticDoc.setMessage(smsEntity.getMessage());
                    elasticDoc.setSentDate(smsEntity.getUpdated_at());
                    phoneServices.create(elasticDoc);
                }
                else {
                    smsEntity.setStatus("API Called Failed");
                    smsEntity.setFaliure_code("500");
                    smsRepository.save(smsEntity);
                }
        }
        else {
            return "Phone Number is Not Found";
        }
        return null;
    }





}
