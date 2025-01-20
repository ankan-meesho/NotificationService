package com.example.demo.smsService.services;


import com.example.demo.smsService.controller.smsController;
import com.example.demo.utils.ApiResponse;
import com.example.demo.smsService.dto.smsDTO;
import com.example.demo.smsService.entity.smsEntity;
import com.example.demo.smsService.dto.smsMapper;
import com.example.demo.smsService.repository.smsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class smsServices {
    @Autowired
    private final com.example.demo.smsService.repository.smsRepository smsRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private final KafkaServices kafkaServices;

    Logger logger= LoggerFactory.getLogger(smsServices.class);


    public smsServices(smsRepository smsRepository, KafkaServices kafkaServices) {
        this.smsRepository = smsRepository;
        this.kafkaServices = kafkaServices;
    }

    /**
     * TODO
     * 1. regex pattern theek kerdo
     * 2. redisTemplate.opsForValue() -> this can throw exception. handle it.
     * 3. StringUtils.isEmpty()
     * @param smsDTO
     * @return
     */
    public ApiResponse sendSms(smsDTO smsDTO) {
        String regexStr = "^[1-9][0-9]{9}$";
        if(Pattern.matches(regexStr,smsDTO.getPhone_number())){
            if(smsDTO.getMessage().isEmpty()){
                logger.error("Message is null or empty");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Message cannot be empty");
            }
            smsEntity smsEntity = smsMapper.mapToEntity(smsDTO);
            String phoneNumberFromRedis=null;
            try {
                phoneNumberFromRedis=redisTemplate.opsForValue().get(smsDTO.getPhone_number());
            }
            catch (RedisSystemException ex){
                logger.error("Redis system error Failed to get the value from the redis "+ex.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Redis system error Failed to get the value from the redis "+ex.getMessage());
            }
            if(phoneNumberFromRedis==null){
                smsEntity.setStatus("Processing Request");
                smsRepository.save(smsEntity);
                smsDTO response= smsMapper.mapToDTO(smsEntity);
                if(kafkaServices.sendMessage(response.getId())){
                    logger.info("Sent to Kafka Service");
                    smsEntity.setStatus("Sent to Kafka Service");
                    smsRepository.save(smsEntity);
                    return new ApiResponse(response.getId(),"Successfully sent", smsDTO.getPhone_number());
                }
                else{
                    logger.error("Couldn't send message from Kafka Service");
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Something went wrong");
                }
            }
            else{
                smsEntity.setStatus("Blacklisted");
                smsEntity.setFaliure_code("500");
                smsEntity.setFaliure_comment("Number is Blacklisted");
                smsRepository.save(smsEntity);
                logger.error("Number is Blacklisted");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Number is Blacklisted");
            }
        }
        else{
            logger.error("Phone number is not valid");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid phone number");
        }

    }



    public smsDTO getDetailsById(String id) {
        Optional<smsEntity> smsEntity = smsRepository.findById(id);
        if(smsEntity.isPresent()){
            logger.info("Deatils found for id "+id);
            return smsMapper.mapToDTO(smsEntity.orElse(new smsEntity()));
        }
        else{
            logger.error("Couldn't find id "+id);
            return new smsDTO(null,null,null,"404","INVALID_REQUEST","request_id not found",null,null);
        }
    }


}
