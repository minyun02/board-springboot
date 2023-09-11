package com.minsproject.board.controller;

import com.minsproject.board.domain.constant.SearchType;
import com.minsproject.board.dto.request.PostRequest;
import com.minsproject.board.dto.response.PostResponse;
import com.minsproject.board.domain.constant.FormStatus;
import com.minsproject.board.dto.security.BoardPrincipal;
import com.minsproject.board.service.PaginationService;
import com.minsproject.board.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/posts")
@Controller
public class PostController {

    private final PostService postService;
    private final PaginationService paginationService;

    @GetMapping
    public String posts(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map) {
        Page<PostResponse> posts = postService.searchPosts(searchType, searchValue, pageable).map(PostResponse::fromPost);
        List<Integer> pageNumbers = paginationService.getPageNumbers(pageable.getPageNumber(), posts.getTotalPages());

        map.addAttribute("posts", posts);
        map.addAttribute("searchTypes", SearchType.values());
//        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);
        map.addAttribute("pageNumbers", pageNumbers);


        return "posts/index";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable Integer postId, Pageable pageable, ModelMap map) {
//        PostWithCommentsResponse post = PostWithCommentsResponse.from(postService.searchPostWithComments( postId));
        PostResponse post = PostResponse.fromPost(postService.searchPost(pageable, postId));

        map.addAttribute("post", post);
        map.addAttribute("totalCount", postService.getTotalNumberOfPosts());

        return "posts/detail";
    }

    @GetMapping("/create")
    public String create(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "posts/form";
    }

    @PostMapping("create")
    public String create(PostRequest request, Authentication authentication) {
        postService.create(authentication.getName(), request.title(), request.body());

        return "redirect:/posts";
    }

    @GetMapping("/modify/{postId}")
    public String modify(@PathVariable Integer postId, Pageable pageable, ModelMap map) {
        PostResponse post = PostResponse.fromPost(postService.searchPost(pageable, postId));

        map.addAttribute("post", post);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "posts/form";
    }

    @PostMapping("/modify/{postId}")
    public String modify(
            @PathVariable Integer postId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            PostRequest request)
    {
        postService.modifyPost(postId, request.toDto(boardPrincipal.toDto()));

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/delete/{postId}")
    public String delete(
            @PathVariable Integer postId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {
        postService.deletePost(postId, boardPrincipal.getUsername());

        return "redirect:/";
    }

}
