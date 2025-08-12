package com.example.VitaDigging.controller;

import com.example.VitaDigging.entity.Product;
import com.example.VitaDigging.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductDetail(
            @PathVariable Long id,
            @RequestParam(required = false) String ageGroup
    ) {
        if (ageGroup != null) {
            return ResponseEntity.ok(productService.getProductByIdWithLogging(id, ageGroup));
        } else {
            return ResponseEntity.ok(productService.getProductDetail(id));
        }
    }

    // ✅ 연령대별 인기 제품 조회
    @GetMapping("/popular")
    public ResponseEntity<List<Product>> getPopularProductsByAgeGroup(@RequestParam String ageGroup) {
        return ResponseEntity.ok(productService.getPopularProductsByAgeGroup(ageGroup));
    }
}