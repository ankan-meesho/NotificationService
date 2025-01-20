package com.example.demo.smsService.repository;

import com.example.demo.smsService.entity.smsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface smsRepository extends JpaRepository<smsEntity,String> {
}
