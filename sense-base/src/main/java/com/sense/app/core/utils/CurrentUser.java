package com.sense.app.core.utils;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.sense.app.core.exception.NoAuthException;
import com.sense.app.core.user.domain.SysUser;

public class CurrentUser {

    public static Long getUserId(){
        return getUser().getId();
    }

    public static Long getUserIdNoException(){
        try{
            return getUser().getId();
        }catch (Exception e){
            return -1L;
        }
    }

    public static String getUserName(){
        return getUser().getUsername();
    }

    public static SysUser getUser(){
        Object userStr = null;
        try{
            userStr = StpUtil.getTokenSession().get("user");
        } catch (SaTokenException e){
            throw new NoAuthException("登录过期，请重新登录！");
        }

        try{
            return JSONUtil.toBean(userStr.toString(), SysUser.class);
        } catch (Exception e){
            throw new NoAuthException("用户信息变化，请重新登录！");
        }
    }

    public static Long getTenantId(){
        try{
            return getUser().getTenantId();
        }catch (Exception e){
            return -1L;
        }
    }

}
