package com.birthdaybot.exceptions;

import lombok.Getter;

@Getter
public class NoAdminRightsException extends RuntimeException{

    private final Long userId;
    private final String userLocate;
    public NoAdminRightsException(Long userId, String userLocate) {
        this.userId = userId;
        this.userLocate = userLocate;
    }
}
