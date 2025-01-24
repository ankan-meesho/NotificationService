package com.example.demo.mockAPI;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class apiBody {
    public JSONObject getJsonbody(String message, String phone_number){
        String apiBody="{" +
                "\"deliverychannel\": sms ," +
                "\"channels\": {" +
                "\"sms\": {" +
                "\"text\": "+ message +" } " +
                "} , " +
                "\"destination\" : [" +
                "{" +
                "\"msisdn\" : ["+ phone_number + "]," +
                "}" +
                "]" +
                "}";
        return new JSONObject(apiBody);
    }
    public JSONObject getJSONHeader(String API_KEY){
        String apiHead="{" +
                "\"apiKey\":"+API_KEY;
        return new JSONObject(apiHead);
    }
}
