package com.sense.app.core.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信端通过手机号验证码登录
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WxTokenRequest {

    // 租户编码
    private String tenantCode;

    // 手机号
    private String phone;

    // 手机验证码
    private String smsCode;

    // 图形验证码ID
    private String captchaId;

    // 图形验证码
    private String captchaCode;

}
