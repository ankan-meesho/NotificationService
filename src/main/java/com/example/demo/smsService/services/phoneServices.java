package com.example.demo.smsService.services;

import com.example.demo.smsService.entity.phoneEntity;
import com.example.demo.smsService.services.serviceInterface.phoneServicesInt;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.demo.smsService.repository.phoneRepository;
import org.springframework.web.server.ResponseStatusException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class phoneServices implements phoneServicesInt {
    @Autowired
    private final phoneRepository phoneRepository;
    @Autowired
    private final RestHighLevelClient restHighLevelClient;

    public phoneServices(phoneRepository phoneRepository, RestHighLevelClient restHighLevelClient) {
        this.phoneRepository = phoneRepository;
        this.restHighLevelClient = restHighLevelClient;
    }
    @Override
    public phoneEntity create(phoneEntity phoneEntity) {
        try{
            return phoneRepository.save(phoneEntity);
        }
        catch(Exception e){
            return null;
        }
    }

    public boolean delete(phoneEntity phoneEntity) {
        try{
            phoneRepository.delete(phoneEntity);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public phoneEntity getphone(String id){
        try {
            Optional<phoneEntity> response = phoneRepository.findById(id);
            if (response.isPresent()) {
                System.out.printf("Response from Kafka Service: %s\n", response);
                return response.orElse(new phoneEntity());
            } else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAllPhoneContainingMessage(String message,int pageNumber,int pageSize) throws IOException {
        if(message==null || message.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message cannot be null or empty");
        }

        int from = (pageNumber - 1) * pageSize;
        SearchRequest searchRequest=new SearchRequest("phone_index");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchPhraseQuery("message",message));
        sourceBuilder.from(from)
                        .size(pageSize);
        searchRequest.source(sourceBuilder);
        SearchResponse response=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        List<Map<String, Object>> results = new ArrayList<>();
        for(org.elasticsearch.search.SearchHit hits:response.getHits()){
            results.add(hits.getSourceAsMap());
        }

        if(results.isEmpty()) return null;
        return results;
        //        Query search=new StringQuery("{\"match_phrase\":{\"message\":{\"query\":\""+message+"\"}}}\"");
//        SearchQuery
//        String jsonQuery = "{\"match_phrase\":{\"" + "message" + "\":{\"query\":\"" + message + "\"}}}";
////        String jsonQuery = "{\"wildcard\":{\"" + "message" + "\":{\"value\":\"" + message + "\"}}}";
//        // Use the StringQuery object to execute the search
//        StringQuery stringQuery = new StringQuery(jsonQuery);
//        SearchHits<phoneEntity> response=elasticsearchTemplate.search(stringQuery, phoneEntity.class, IndexCoordinates.of("phone_index"));

    }

    @Override
    public List<Map<String, Object>> getAllPhoneMessage(String phone, String startTime, String endTime, int pageNumber, int pageSize) throws IOException {
        String regexStr = "^[1-9][0-9]{9}$";
        if(phone==null || phone.isEmpty() ||!Pattern.matches(regexStr, phone) || startTime==null || endTime==null || endTime.isEmpty() || startTime.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Query");
        }

        if(pageNumber<1 || pageSize<1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number/Page Size cannot be less than 1");
        }


        int from=(pageNumber-1)*pageSize;

        SearchRequest searchRequest=new SearchRequest("phone_index");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("phoneNumber", phone))
                .must(QueryBuilders.rangeQuery("sentDate")
                        .gte(startTime)
                        .lte(endTime))
        );

        sourceBuilder.from(from)
                .size(pageSize)
                .sort("sentDate", SortOrder.DESC);

        searchRequest.source(sourceBuilder);
        SearchResponse response= restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String, Object>> results = new ArrayList<>();
        for(SearchHit hits:response.getHits()){
            results.add(hits.getSourceAsMap());
        }

        if(results.isEmpty()) return null;
        return results;


//                "\"bool\": { " +
//                "\"must\": [ { \"match\": { \"" + matchField + "\": \"" + matchValue + "\" } } ], " +
//                "\"filter\": [ { \"range\": { \"" + rangeField + "\": { \"gte\": \"" + fromValue + "\", \"lte\": \"" + toValue + "\" } } } ] " +
//                "} }";
//        JSONObject range = new JSONObject();
//        range.put("gte", startTime);
//        range.put("lte", endTime);
//
//        JSONObject rangeFilter = new JSONObject();
//        rangeFilter.put("sentDate", range);
//
//        JSONObject match = new JSONObject();
//        match.put("phoneNumber", phone);
//
//        JSONObject mustClause = new JSONObject();
//        mustClause.put("match", match);
//
//        JSONObject boolQuery = new JSONObject();
//        boolQuery.put("must", new JSONArray().put(mustClause));
//        boolQuery.put("filter", new JSONArray().put(new JSONObject().put("range", rangeFilter)));
//
//        JSONObject query = new JSONObject();
//        query.put("bool", boolQuery);
//
//        JSONObject order = new JSONObject();
//        order.put("order","asc");
//        order.put("format", "strict_date_optional_time_nanos");
//
//        JSONObject timestamp=new JSONObject();
//        timestamp.put("@timestamp", order);
//
//        JSONObject finalQuery = new JSONObject();
//        finalQuery.put("query", query);
//        finalQuery.put("sort", new JSONArray().put(timestamp));
//        finalQuery.put("from", from);
//        finalQuery.put("size", size);
//
//
//        System.out.println(finalQuery);
//        String querys ="{\n" +
//                "  \"query\": {\n" +
//                "    \"bool\": {\n" +
//                "      \"must\": [\n" +
//                "        {\n" +
//                "          \"match\": {\n" +
//                "            \"phoneNumber\": \"" + phone + "\"\n" +
//                "          }\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"range\": {\n" +
//                "            \"sentDate\": {\n" +
//                "              \"gte\": \"" + startTime + "\",\n" +
//                "              \"lte\": \"" + endTime + "\"\n" +
//                "            }\n" +
//                "          }\n" +
//                "        }\n" +
//                "      ]\n" +
//                "    }\n" +
//                "  },\n" +
//                "  \"from\": " + from + ",\n" +
//                "  \"size\": " + size + "\n" +
//                "}";
//        StringQuery stringQuery = new StringQuery(querys);
//        SearchHits<phoneEntity> response=elasticsearchTemplate.search(stringQuery, phoneEntity.class);

    }
}
