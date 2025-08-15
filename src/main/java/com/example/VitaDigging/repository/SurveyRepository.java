package com.example.VitaDigging.repository;

import com.example.VitaDigging.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Survey> findFirstByUserIdOrderByIdDesc(String userId);
}
