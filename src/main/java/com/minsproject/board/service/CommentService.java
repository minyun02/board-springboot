package com.minsproject.board.service;

import com.minsproject.board.domain.entity.CommentEntity;
import com.minsproject.board.domain.entity.PostEntity;
import com.minsproject.board.domain.entity.UserEntity;
import com.minsproject.board.dto.CommentDto;
import com.minsproject.board.exception.BoardException;
import com.minsproject.board.repository.CommentEntityRepository;
import com.minsproject.board.repository.PostEntityRepository;
import com.minsproject.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
    private final PostEntityRepository postEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final UserEntityRepository userEntityRepository;
    public void saveComment(CommentDto dto) {
        try {
            PostEntity post = postEntityRepository.getReferenceById(dto.postId());
            UserEntity user = userEntityRepository.getReferenceById(dto.userId());
            CommentEntity comment = dto.toEntity(post, user);

            if (dto.parentCommentId() != null) {
                CommentEntity parentComment = commentEntityRepository.getReferenceById(dto.parentCommentId());
                parentComment.addChildComment(comment);
            } else {
                commentEntityRepository.save(comment);
            }
        } catch (BoardException e) {
            log.warn("댓글 저장 실패. - {}", e.getLocalizedMessage());
        }
    }

    public void deleteComment(Integer commentId, Integer userId) {
        commentEntityRepository.deleteByIdAndUser_Id(commentId, userId);
    }
}
