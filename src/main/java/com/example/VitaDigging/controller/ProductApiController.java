package com.example.VitaDigging.controller;

import com.example.VitaDigging.service.ProductApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//테스트 영양제 api 연결
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/api")
public class ProductApiController {

    private final ProductApiService productApiService;

    @PostMapping("/import")
    public ResponseEntity<String> importProducts() {
        try {
            productApiService.fetchAndSaveProducts();
            return ResponseEntity.ok("상품 데이터를 성공적으로 불러왔습니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 로그 출력
            return ResponseEntity.status(500).body("상품 데이터를 불러오는 중 오류가 발생했습니다.");
        }
    }
}