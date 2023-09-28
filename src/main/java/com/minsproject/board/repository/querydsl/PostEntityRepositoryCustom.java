package com.minsproject.board.repository.querydsl;

import com.minsproject.board.domain.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface PostEntityRepositoryCustom {

    Page<PostEntity> findByHashtags(Collection<String> hashtagNames, Pageable pageable);
}
