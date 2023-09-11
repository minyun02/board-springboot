package com.minsproject.board.domain.constant;

import lombok.Getter;

public enum SearchType {
    TITLE("제목"),
    BODY("본문"),
    USERNAME("유저명");
//    HASHTAG("해시태그");

    @Getter private final String description;

    SearchType(String description) {
        this.description = description;
    }

}
