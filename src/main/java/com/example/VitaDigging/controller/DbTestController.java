package com.example.VitaDigging.controller;

import com.example.VitaDigging.entity.UserEntity;
import com.example.VitaDigging.service.SimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/simple")
public class DbTestController {

    @Autowired
    SimpleService simpleService;

    @GetMapping("/db/search/{name}")
    public ResponseEntity<List<String>> searchName(@PathVariable String name){
        List<String> names = simpleService.searchName(name);

        if(names == null){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(names);
    }

    @PostMapping("/db/insert")
    public ResponseEntity<UserEntity> insertData(@RequestBody Map<String, String> request){
        String name = request.get("name");
        if(name == null || name.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        UserEntity ret = simpleService.insertSimpleEntity(name);
        return ResponseEntity.status(200).body(ret);
    }

}