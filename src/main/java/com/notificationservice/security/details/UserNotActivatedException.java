package com.notificationservice.security.details;

import org.springframework.security.core.AuthenticationException;

class UserNotActivatedException extends AuthenticationException {

     UserNotActivatedException(String msg) {
        super(msg);
    }
}
