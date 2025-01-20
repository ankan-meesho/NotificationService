package com.example.demo.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.demo.utils.AppConstant;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.demo.smsService")
public class appConfig {


    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

//      ConnectionFactory
        template.setConnectionFactory(connectionFactory());

//      Key Serializer
        template.setKeySerializer(new StringRedisSerializer());

//      Value Serializer
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }


    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient restClient= RestClient.builder(
                new HttpHost("localhost", 9200)
        ).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);
        return client ;
    }

}
