package com.minsproject.board.controller;

import com.minsproject.board.dto.request.UserJoinRequest;
import com.minsproject.board.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(ModelMap map) {
        return "auth/signup";
    }

    @PostMapping("/signup")
//    public String signup(String username, String password, ModelMap map) {
    public String signup(UserJoinRequest request, ModelMap map) {
//        map.addAttribute("result", Response.success(UserJoinResponse.from(userService.join(username, password))));
//        userService.join(username, password);
        userService.join(request.username(), request.password(), request.nickname());

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
