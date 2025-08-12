package com.example.VitaDigging.repository;

import com.example.VitaDigging.entity.Product;
import com.example.VitaDigging.entity.ProductView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductViewRepository extends JpaRepository<ProductView, Long> {

    // 연령대별 인기 제품 TOP 5 조회 (조회수 기준)
    @Query(value = "SELECT * FROM products p " +
            "WHERE p.id IN ( " +
            "  SELECT product_id FROM product_view " +
            "  WHERE age_group = :ageGroup " +
            "  GROUP BY product_id " +
            "  ORDER BY COUNT(id) DESC " +
            ")", nativeQuery = true)
    List<Product> findPopularByAgeGroup(@Param("ageGroup") String ageGroup, Pageable pageable);
}

