package com.minsproject.board.repository;

import com.minsproject.board.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {

    void deleteByIdAndUser_Id(Integer commentId, Integer userId);
}
