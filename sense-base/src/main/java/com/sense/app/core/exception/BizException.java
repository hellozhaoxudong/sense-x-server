package com.sense.app.core.exception;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @ClassName BizException
 * @Description 基础异常
 * @Author zhaoxudong
 * @Date 2022/6/2
 **/
@Data
public class BizException extends RuntimeException{
    private String errMsg;

    public BizException(String errMsg) {
        this.errMsg = errMsg;
    }

    public BizException(String errMsg, Throwable cause) {
        super(cause);
        this.errMsg = errMsg;
    }

    public BizException(String errMsg, Object... args) {
        this.errMsg = StrUtil.format(errMsg, args);
    }

    public BizException(String errMsg, Throwable cause, Object... args) {
        super(cause);
        this.errMsg = StrUtil.format(errMsg, args);
    }
}
