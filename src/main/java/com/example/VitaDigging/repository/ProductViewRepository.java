package com.example.VitaDigging.repository;

import com.example.VitaDigging.entity.Product;
import com.example.VitaDigging.entity.ProductView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductViewRepository extends JpaRepository<ProductView, Long> {

    // 연령대별 인기 제품 조회 (조회수 기준, Pageable 적용 가능)
    @Query(value = "SELECT p.* FROM products p " +
            "JOIN product_view v ON p.id = v.product_id " +
            "WHERE v.age_group = :ageGroup " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(v.id) DESC \n-- #pageable\n",
            countQuery = "SELECT COUNT(DISTINCT p.id) FROM products p " +
                    "JOIN product_view v ON p.id = v.product_id " +
                    "WHERE v.age_group = :ageGroup",
            nativeQuery = true)
    List<Product> findPopularByAgeGroup(@Param("ageGroup") String ageGroup, Pageable pageable);
}

