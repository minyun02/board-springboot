package com.minsproject.board.repository;

import com.minsproject.board.domain.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {
    List<LikeEntity> findAllByPostId(Integer postId);
    LikeEntity findByPostIdAndUserId(Integer postId, Integer userId);
}