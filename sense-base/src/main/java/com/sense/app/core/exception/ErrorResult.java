package com.sense.app.core.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResult {

    /**
     * HTTP状态码
     */
    private int code;

    /**
     * 异常信息
     */
    private String error;

}
