package com.minsproject.board.service;

import com.minsproject.board.dto.PostWithCommentsDto;
import com.minsproject.board.exception.ErrorCode;
import com.minsproject.board.exception.BoardException;
import com.minsproject.board.domain.constant.SearchType;
import com.minsproject.board.dto.PostDto;
import com.minsproject.board.domain.entity.PostEntity;
import com.minsproject.board.domain.entity.UserEntity;
import com.minsproject.board.repository.PostEntityRepository;
import com.minsproject.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserEntityRepository userEntityRepository;

    private final PostEntityRepository postEntityRepository;

    @Transactional
    public void create(String username, String title, String body) {
        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new BoardException(ErrorCode.USER_NOT_FOUND, String.format("username = %s", username)));

        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }

    public Page<PostDto> searchPosts(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchType == null || searchKeyword.isBlank()) {
            return postEntityRepository.findAll(pageable).map(PostDto::fromEntity);
        }

        return switch (searchType) {
            case TITLE -> postEntityRepository.findByTitleContaining(searchKeyword, pageable).map(PostDto::fromEntity);
            case BODY -> postEntityRepository.findByBodyContaining(searchKeyword, pageable).map(PostDto::fromEntity);
            case USERNAME -> postEntityRepository.findByUserUsernameContaining(searchKeyword, pageable).map(PostDto::fromEntity);
        };
    }

    public PostDto searchPost(Pageable pageable, Integer postId) {
        PostDto post = postEntityRepository.findById(postId)
                .map(PostDto::fromEntity)
                .orElseThrow(
                () -> new BoardException(ErrorCode.POST_NOT_FOUND, String.format("postId = %s", postId))
        );
        return post;
    }

    public long getTotalNumberOfPosts() {
        return postEntityRepository.count();
    }

    public void modifyPost(Integer postId, PostDto dto) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new BoardException(ErrorCode.POST_NOT_FOUND, String.format("postId = %d", postId)));
        if (!Objects.equals(postEntity.getUser().getId(), dto.user().getId())) {
            throw new BoardException(ErrorCode.INVALID_PERMISSION, String.format("%d 게시글에 접근 권한이 없습니다. username = $s", postId, dto.user().getId()));
        }

        postEntity.setTitle(dto.title());
        postEntity.setBody(dto.body());
        postEntity.setUpdatedAt(LocalDateTime.now());

        postEntityRepository.saveAndFlush(postEntity);
    }

    public void deletePost(Integer postId, String username) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new BoardException(ErrorCode.POST_NOT_FOUND, String.format("postId = $d", postId)));
        if (!Objects.equals(postEntity.getUser().getUsername(), username)) {
            throw new BoardException(ErrorCode.INVALID_PERMISSION, String.format("%d 게시글에 접근 권한이 없습니다. username = $s", postId, username));
        }

        postEntityRepository.delete(postEntity);
    }

    public PostWithCommentsDto searchPostWithComments(Integer postId) {
        return postEntityRepository.findById(postId)
                .map(PostWithCommentsDto::from)
                .orElseThrow(() -> new BoardException(ErrorCode.POST_NOT_FOUND, String.format("게시글이 없습니다 - postId = %d", postId)));
    }
}
