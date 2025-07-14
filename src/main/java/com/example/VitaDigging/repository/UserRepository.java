package com.example.VitaDigging.repository;

import com.example.VitaDigging.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, String> {

    Optional<UserEntity> findUserById(String id); // 아이디 조회

    boolean existsUserIdById(String id); // 아이디 중복 확인

}
