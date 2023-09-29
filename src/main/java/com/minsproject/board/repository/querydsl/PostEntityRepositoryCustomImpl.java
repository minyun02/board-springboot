package com.minsproject.board.repository.querydsl;

import com.minsproject.board.domain.entity.PostEntity;
import com.minsproject.board.domain.entity.QHashtagEntity;
import com.minsproject.board.domain.entity.QPostEntity;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Collection;
import java.util.List;

public class PostEntityRepositoryCustomImpl extends QuerydslRepositorySupport implements PostEntityRepositoryCustom {

    public PostEntityRepositoryCustomImpl() { super(PostEntity.class); }

    @Override
    public Page<PostEntity> findByHashtag(String hashtagName, Pageable pageable) {
        QHashtagEntity hashtag = QHashtagEntity.hashtagEntity;
        QPostEntity post = QPostEntity.postEntity;

        JPQLQuery<PostEntity> query = from(post)
                .innerJoin(post.hashtags, hashtag)
                .where(hashtag.hashtagName.containsIgnoreCase(hashtagName));
        List<PostEntity> posts = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(posts, pageable, query.fetchCount());
    }

    @Override
    public Page<PostEntity> findByHashtags(Pageable pageable) {
        return findByHashtags(null, pageable);
    }

    @Override
    public Page<PostEntity> findByHashtags(Collection<String> hashtagNames, Pageable pageable) {
        QHashtagEntity hashtag = QHashtagEntity.hashtagEntity;
        QPostEntity post = QPostEntity.postEntity;

        JPQLQuery<PostEntity> query = from(post)
                .innerJoin(post.hashtags, hashtag);

        if (hashtagNames != null) {
            query.where(hashtag.hashtagName.in(hashtagNames));
        }

        List<PostEntity> posts = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(posts, pageable, query.fetchCount());
    }
}
