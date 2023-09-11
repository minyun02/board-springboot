package com.minsproject.board.repository;


import com.minsproject.board.domain.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {
    Page<PostEntity> findByTitleContaining(String title, Pageable pageable);
    Page<PostEntity> findByBodyContaining(String body, Pageable pageable);
    Page<PostEntity> findByUserUsernameContaining(String searchKeyword, Pageable pageable);
}
