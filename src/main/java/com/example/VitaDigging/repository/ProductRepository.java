package com.example.VitaDigging.repository;

import com.example.VitaDigging.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);
    List<Product> findByCategory(String category);

}
