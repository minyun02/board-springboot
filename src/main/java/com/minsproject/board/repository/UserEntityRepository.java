package com.minsproject.board.repository;

import com.minsproject.board.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);
}
