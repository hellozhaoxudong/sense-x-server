package com.sense.app.core.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenResquest {

    // 登录方式：password
    private String grantType;

    // 登录名
    private String username;

    // 密码
    private String password;

}
