package com.minsproject.board.controller;

import com.minsproject.board.domain.constant.ErrorCode;
import com.minsproject.board.dto.request.UserAuthRequest;
import com.minsproject.board.dto.response.AlarmResponse;
import com.minsproject.board.dto.security.BoardPrincipal;
import com.minsproject.board.exception.BoardException;
import com.minsproject.board.service.PaginationService;
import com.minsproject.board.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;
    private final PaginationService paginationService;

    @GetMapping("/signup")
    public String signup(UserAuthRequest userAuthRequest) {

        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(UserAuthRequest userAuthRequest, BindingResult bindingResult) {
        if (userService.checkDuplicatedUsername(userAuthRequest.username())) {
            bindingResult.addError(new FieldError("userJoinRequest", "username", new BoardException(ErrorCode.DUPLICATED_USER_NAME).getLocalizedMessage()));
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResults = {}", bindingResult);
            return "auth/signup";
        }

        userService.join(userAuthRequest);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/alarm")
    public String alarm(@AuthenticationPrincipal BoardPrincipal boardPrincipal,
                        ModelMap map,
                        @PageableDefault(sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("getAlarm from /alarm");
        Page<AlarmResponse> alarms = userService.getAlarm(boardPrincipal.id(), pageable).map(AlarmResponse::from);
        List<Integer> pageNumbers = paginationService.getPageNumbers(pageable.getPageNumber(), alarms.getTotalPages());

        map.addAttribute("alarms", alarms);
        map.addAttribute("pageNumbers", pageNumbers);

        return "posts/alarm";
    }
}
