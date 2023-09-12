package com.minsproject.board.controller;

import com.minsproject.board.dto.request.CommentRequest;
import com.minsproject.board.dto.security.BoardPrincipal;
import com.minsproject.board.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@RequestMapping("/comments")
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/new")
    public String createComment(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            CommentRequest commentRequest) {

        commentService.saveComment(commentRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/posts/" + commentRequest.postId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(
            @PathVariable Integer commentId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            Integer postId
    ) {
        commentService.deleteComment(commentId, boardPrincipal.id());

        return "redirect:/posts/" + postId;
    }
}
