package com.minsproject.board.repository;

import com.minsproject.board.domain.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface HashtagEntityRepository extends JpaRepository<HashtagEntity, Integer> {

    List<HashtagEntity> findByHashtagNameIn(Set<String> hashtagNames);

}
