package org.devjeans.sid.global.util;

import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.devjeans.sid.global.exception.exceptionType.AuthException.UNAUTHENTICATED_USER;

@Component
public class SecurityUtil {
    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            return Long.parseLong(authentication.getName());
        } catch(Exception e) {
            throw new BaseException(UNAUTHENTICATED_USER);
        }
    }

}
