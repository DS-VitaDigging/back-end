package com.example.VitaDigging.service;

import com.example.VitaDigging.entity.Product;
import com.example.VitaDigging.entity.ProductView;
import com.example.VitaDigging.repository.ProductRepository;
import com.example.VitaDigging.repository.ProductViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductViewRepository productViewRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product getProductDetail(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 제품을 찾을 수 없습니다."));
    }

    // ✅ 제품 상세 조회 시 조회 기록 저장
    public Product getProductByIdWithLogging(Long id, String ageGroup) {
        ProductView view = ProductView.builder()
                .productId(id)
                .ageGroup(ageGroup)
                .build();
        productViewRepository.save(view);

        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 제품을 찾을 수 없습니다."));
    }

    // ✅ 연령대별 인기 제품 반환
    public List<Product> getPopularProductsByAgeGroup(String ageGroup) {
        return productViewRepository.findPopularByAgeGroup(ageGroup, PageRequest.of(0, 5));
    }
}
