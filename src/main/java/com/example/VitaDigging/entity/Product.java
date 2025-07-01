package com.example.VitaDigging.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String efficacy;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(columnDefinition = "TEXT")
    private String precautions;

    @Column(columnDefinition = "TEXT")
    private String manufacturer;      // 제조사

    @Column(columnDefinition = "TEXT")
    private String purchaseLink;
}

