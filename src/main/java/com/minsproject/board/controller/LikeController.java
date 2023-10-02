package com.minsproject.board.controller;

import com.minsproject.board.dto.LikeDto;
import com.minsproject.board.dto.response.LikeResponse;
import com.minsproject.board.dto.security.BoardPrincipal;
import com.minsproject.board.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/likes")
@RestController
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/{postId}")
    public LikeResponse getLikes(@PathVariable Integer postId,
                                 @AuthenticationPrincipal BoardPrincipal boardPrincipal)
    {

        List<LikeDto> likeDtos = likeService.getLikes(postId);
        Boolean hasLikedBefore = likeService.hasLikedBefore(likeDtos, boardPrincipal.id());

        return LikeResponse.of(likeDtos.size(), hasLikedBefore);
    }

    @PostMapping("/{postId}")
    public void saveLikes(@PathVariable Integer postId,
                                  @AuthenticationPrincipal BoardPrincipal boardPrincipal)
    {
        likeService.saveLike(postId, boardPrincipal.id());
    }

    @DeleteMapping("/{postId}")
    public void removeLike(@PathVariable Integer postId,
                           @AuthenticationPrincipal BoardPrincipal boardPrincipal)
    {
        likeService.removeLike(postId, boardPrincipal.id());
    }
}
