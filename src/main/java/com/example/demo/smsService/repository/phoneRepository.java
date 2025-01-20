package com.example.demo.smsService.repository;

import com.example.demo.smsService.entity.phoneEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface phoneRepository extends ElasticsearchRepository<phoneEntity,String> {
}
