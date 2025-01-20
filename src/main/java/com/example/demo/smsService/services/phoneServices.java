package com.example.demo.smsService.services;

import com.example.demo.smsService.entity.phoneEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import com.example.demo.smsService.repository.phoneRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class phoneServices {
    @Autowired
    private final phoneRepository phoneRepository;
    @Autowired
    private final ElasticsearchTemplate elasticsearchTemplate;

    public phoneServices(phoneRepository phoneRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.phoneRepository = phoneRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }
    public void create(phoneEntity phoneEntity) {
        phoneRepository.save(phoneEntity);
    }

    public phoneEntity getphone(String id){
        Optional<phoneEntity> response = phoneRepository.findById(id);
        System.out.printf("Response from Kafka Service: %s\n",response);
        return response.orElse(new phoneEntity());
    }

    public List<phoneEntity> getAllPhoneContainingMessage(String message){
//        Query search=new StringQuery("{\"match_phrase\":{\"message\":{\"query\":\""+message+"\"}}}\"");
//        SearchQuery
        String jsonQuery = "{\"match_phrase\":{\"" + "message" + "\":{\"query\":\"" + message + "\"}}}";
//        String jsonQuery = "{\"wildcard\":{\"" + "message" + "\":{\"value\":\"" + message + "\"}}}";
        // Use the StringQuery object to execute the search
        StringQuery stringQuery = new StringQuery(jsonQuery);
        SearchHits<phoneEntity> response=elasticsearchTemplate.search(stringQuery, phoneEntity.class, IndexCoordinates.of("phone_index"));
        List<phoneEntity>result = new ArrayList<>();
        for(SearchHit<phoneEntity> searchHit:response){
            phoneEntity phone=searchHit.getContent();
            result.add(phone);
        }
        return result;
    }

    public List<phoneEntity> getAllPhoneMessage(String phone,String startTime,String endTime,int page,int size){
        String matchField="phoneNumber";
        String matchValue=phone;
        String rangeField="sentDate";
        String fromValue=startTime;
        String toValue=endTime;
        int from=(page-1)*size;
        String jsonQuery="{ \"bool\": { " +
                "\"must\": [ { \"match\": { \"" + matchField + "\": \"" + matchValue + "\" } } ], " +
                "\"filter\": [ { \"range\": { \"" + rangeField + "\": { \"gte\": \"" + fromValue + "\", \"lte\": \"" + toValue + "\" } } } ] " +
                "} }";
        StringQuery stringQuery = new StringQuery(jsonQuery);
        SearchHits<phoneEntity> response=elasticsearchTemplate.search(stringQuery, phoneEntity.class);
        List<phoneEntity>result = new ArrayList<>();
        for(SearchHit<phoneEntity> searchHit:response){
            phoneEntity phone1=searchHit.getContent();
            result.add(phone1);
        }
        return result;
    }
}
