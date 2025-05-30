package com.example.VitaDigging.repository;

import com.example.VitaDigging.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimpleEntityRepository extends JpaRepository<UserEntity, Long> {

    public List<UserEntity> findByName(String name);
}