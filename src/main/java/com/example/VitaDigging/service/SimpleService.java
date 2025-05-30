package com.example.VitaDigging.service;

import com.example.VitaDigging.entity.UserEntity;
import com.example.VitaDigging.repository.SimpleEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleService {

    @Autowired
    SimpleEntityRepository simpleEntityRepository;

    public UserEntity insertSimpleEntity(String name) {
        // 중복 검사 제거
        return simpleEntityRepository.save(new UserEntity(name));
    }

    public List<String> searchName(String name) {
        List<UserEntity> list = simpleEntityRepository.findByName(name);
        List<String> ret = new ArrayList<>();

        for(UserEntity se : list){
            ret.add(se.getName());
        }
        return ret;
    }
}
