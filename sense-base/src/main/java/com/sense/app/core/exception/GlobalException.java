package com.sense.app.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName GlobalException
 * @Description 全局异常处理
 * @Author zhaoxudong
 * @Date 2022/6/2
 **/
@Slf4j
@RestControllerAdvice
public class GlobalException {

    /**
     * 业务异常
     */
    @ExceptionHandler(value = BizException.class)
    public ResponseEntity<ErrorResult> baseExceptionHandler(BizException e) {
        return new ResponseEntity<>(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getErrMsg()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 权限异常
     */
    @ExceptionHandler(value = NoAuthException.class)
    public ResponseEntity<ErrorResult> noAuthExceptionHandler(NoAuthException e) {
        log.error("### Error ### {}", e.getErrMsg());
        return new ResponseEntity<>(new ErrorResult(HttpStatus.UNAUTHORIZED.value(), e.getErrMsg()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * 未捕获异常
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResult> exceptionHandler(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
