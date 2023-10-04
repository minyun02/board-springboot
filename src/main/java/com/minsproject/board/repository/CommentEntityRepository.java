package com.minsproject.board.repository;

import com.minsproject.board.domain.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {

    void deleteByIdAndUser_Id(Integer commentId, Integer userId);
    Page<CommentEntity> findAllByUserId(Integer userId, Pageable pageable);
}
