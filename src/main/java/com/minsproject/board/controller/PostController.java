package com.minsproject.board.controller;

import com.minsproject.board.domain.constant.SearchType;
import com.minsproject.board.dto.request.PostRequest;
import com.minsproject.board.dto.response.CommentResponse;
import com.minsproject.board.dto.response.PostResponse;
import com.minsproject.board.domain.constant.FormStatus;
import com.minsproject.board.dto.response.PostWithCommentsResponse;
import com.minsproject.board.dto.security.BoardPrincipal;
import com.minsproject.board.service.LikeService;
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
import java.util.Set;

@AllArgsConstructor
@RequestMapping("/posts")
@Controller
public class PostController {

    private final LikeService likeService;
    private final PostService postService;
    private final PaginationService paginationService;

    @GetMapping
    public String posts(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map) {
        Page<PostResponse> posts = postService.searchPosts(searchType, searchValue, pageable).map(PostResponse::from);
        List<Integer> pageNumbers = paginationService.getPageNumbers(pageable.getPageNumber(), posts.getTotalPages());

        map.addAttribute("posts", posts);
        map.addAttribute("searchTypes", SearchType.values());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);
        map.addAttribute("pageNumbers", pageNumbers);

        return "posts/index";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable Integer postId, ModelMap map) {
        PostWithCommentsResponse post = PostWithCommentsResponse.from(postService.searchPostWithComments(postId));

        map.addAttribute("post", post);
        map.addAttribute("comments", post.commentResponses());
        map.addAttribute("totalCount", postService.getTotalNumberOfPosts());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);

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
        PostResponse post = PostResponse.from(postService.searchPost(pageable, postId));

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

    @GetMapping("/search-hashtag")
    public String searchHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<PostResponse> posts = postService.searchPostsViaHashtag(searchValue, pageable).map(PostResponse::from);
        Set<String> hashtags = postService.getAllHashtags();
        List<Integer> pageNumbers = paginationService.getPageNumbers(pageable.getPageNumber(), posts.getTotalPages());

        map.addAttribute("posts", posts);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("pageNumbers", pageNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);

        return "posts/search-hashtag";
    }

    @GetMapping("/my")
    public String myPostsAndComments(@AuthenticationPrincipal BoardPrincipal boardPrincipal,
                          @PageableDefault(sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable,
                          ModelMap map)
    {

        Page<PostResponse> myPosts = postService.searchMyPosts(boardPrincipal.id(), pageable).map(PostResponse::from);
        Page<CommentResponse> myComments = postService.searchMyComments(boardPrincipal.id(), pageable).map(CommentResponse::from);
        List<Integer> postsPageNumbers = paginationService.getPageNumbers(pageable.getPageNumber(), myPosts.getTotalPages());
        List<Integer> commentsPageNumbers = paginationService.getPageNumbers(pageable.getPageNumber(), myComments.getTotalPages());

        map.addAttribute("myPosts", myPosts);
        map.addAttribute("myComments", myComments);
        map.addAttribute("postsPageNumbers", postsPageNumbers);
        map.addAttribute("commentsPageNumbers", commentsPageNumbers);

        return "posts/my";
    }
}
