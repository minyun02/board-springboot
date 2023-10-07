package com.minsproject.board.service;

import com.minsproject.board.domain.constant.AlarmType;
import com.minsproject.board.domain.entity.AlarmEntity;
import com.minsproject.board.domain.entity.LikeEntity;
import com.minsproject.board.domain.entity.PostEntity;
import com.minsproject.board.domain.entity.UserEntity;
import com.minsproject.board.dto.LikeDto;
import com.minsproject.board.exception.BoardException;
import com.minsproject.board.exception.ErrorCode;
import com.minsproject.board.repository.AlarmEntityRepository;
import com.minsproject.board.repository.LikeEntityRepository;
import com.minsproject.board.repository.PostEntityRepository;
import com.minsproject.board.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final AlarmService alarmService;
    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    public Boolean hasLikedBefore(List<LikeDto> likeDtos, Integer userId) {
        List<LikeDto> list = likeDtos.stream().filter(like -> like.userId().equals(userId)).collect(Collectors.toList());

        return list.isEmpty();
    }

    public List<LikeDto> getLikes(Integer postId) {
        return likeEntityRepository.findAllByPostId(postId).stream().map(LikeDto::from).toList();
    }

    public void saveLike(Integer postId, Integer userId) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new BoardException(ErrorCode.USER_NOT_FOUND, String.format("userId = %d", userId)));
        PostEntity post = postEntityRepository.findById(postId)
                .orElseThrow(() -> new BoardException(ErrorCode.POST_NOT_FOUND, String.format("postId = %d", postId)));

        likeEntityRepository.save(LikeEntity.of(user, post));

        AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(post.getUser(), AlarmType.NEW_LIKE, userId, postId));
        alarmService.send(alarmEntity.getId(), post.getUser().getId());
    }

    @Transactional
    public void removeLike(Integer postId, Integer userId) {
        LikeEntity entity = likeEntityRepository.findByPostIdAndUserId(postId, userId);
        PostEntity post = postEntityRepository.findById(postId)
                .orElseThrow(() -> new BoardException(ErrorCode.POST_NOT_FOUND, String.format("postId = %d", postId)));

        likeEntityRepository.deleteById(entity.getId());
    }
}
