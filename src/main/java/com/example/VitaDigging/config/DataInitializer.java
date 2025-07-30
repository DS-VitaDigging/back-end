package com.example.VitaDigging.config;

import com.example.VitaDigging.service.ProductApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class DataInitializer implements CommandLineRunner {
//
//    private final ProductApiService productApiService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("외부 API에서 상품 데이터를 불러오는 중...");
//        productApiService.fetchAndSaveProducts();
//        System.out.println("상품 데이터 저장 완료!");
//    }
//}