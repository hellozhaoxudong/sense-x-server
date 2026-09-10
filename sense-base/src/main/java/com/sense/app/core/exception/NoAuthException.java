package com.sense.app.core.exception;

import lombok.Data;

/**
 * 未登录异常
 **/
@Data
public class NoAuthException extends RuntimeException{

    private String errMsg;

    public NoAuthException(String errMsg) {
        this.errMsg = errMsg;
    }

}
