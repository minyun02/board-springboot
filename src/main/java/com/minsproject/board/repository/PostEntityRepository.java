package com.minsproject.board.repository;


import com.minsproject.board.domain.entity.HashtagEntity;
import com.minsproject.board.domain.entity.PostEntity;
import com.minsproject.board.domain.entity.QPostEntity;
import com.minsproject.board.dto.PostDto;
import com.minsproject.board.repository.querydsl.PostEntityRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEntityRepository extends
        JpaRepository<PostEntity, Integer>,
        PostEntityRepositoryCustom
        ,
        QuerydslPredicateExecutor<PostEntity>,
        QuerydslBinderCustomizer<QPostEntity>
{
    Page<PostEntity> findByTitleContaining(String title, Pageable pageable);
    Page<PostEntity> findByBodyContaining(String body, Pageable pageable);
    Page<PostEntity> findByUserUsernameContaining(String searchKeyword, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QPostEntity root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.body, root.hashtags, root.registeredAt);
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '${v}' , first() -> argument를 하나만 받는다.
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%${v}%'
        bindings.bind(root.body).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtags.any().hashtagName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.registeredAt).first(DateTimeExpression::eq);
    }
}
