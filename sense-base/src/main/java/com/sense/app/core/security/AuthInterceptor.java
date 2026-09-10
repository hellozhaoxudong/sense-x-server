package com.sense.app.core.security;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.sense.app.core.exception.NoAuthException;
import com.sense.app.core.utils.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        String method = request.getMethod();

        // 登录接口不需要鉴权
        if (StrUtil.contains(url, "/api/sense/oauth/token")) {
            return true;
        }

        // 检查是否登录
        try {
            StpUtil.checkLogin();
        } catch (SaTokenException e){
            throw new NoAuthException("用户未登录！");
        }

        // 获取用户信息
        Long userId = CurrentUser.getUserId();

        // 检查API是否有权限

        return true;
    }


}
