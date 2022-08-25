package com.hczq.hz.security.permit.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * @author zhust
 */
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = 5022575393500654458L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
