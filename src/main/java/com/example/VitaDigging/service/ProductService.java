package com.example.VitaDigging.service;

import com.example.VitaDigging.entity.Product;
import com.example.VitaDigging.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductDetail(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 제품을 찾을 수 없습니다."));
    }
}