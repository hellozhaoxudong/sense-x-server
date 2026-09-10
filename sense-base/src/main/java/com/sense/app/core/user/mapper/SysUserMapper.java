package com.sense.app.core.user.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.core.user.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName SysUserMapper
 * @description 用户管理Mapper(忽略租户)
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * queryUserByLoginname : 根据登录名查询用户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param loginName 登录名
     */
    SysUser queryUserByLoginname(@Param("loginName") String loginName);

    /**
     * queryUserByPhone : 根据手机号查询用户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param phone 手机号
     */
    SysUser queryUserByPhone(@Param("phone") String phone);

    /**
     * queryUserByEmail : 根据邮箱查询用户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param  email 邮箱
     */
    SysUser queryUserByEmail(@Param("email") String email);

    /**
     * queryUsers : 查询指定租户下的所有用户
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param mybatisPage   分页信息
     * @param tenantId      租户ID
     * @param username     登录名
     * @param name          姓名
     * @param phone         手机号
     * @param enabled       启用状态
     */
    List<SysUser> queryUsers(Page mybatisPage, @Param("tenantId") Long tenantId, @Param("username") String username, @Param("name") String name, @Param("phone") String phone, @Param("enabled") Integer enabled);

    /**
     * updateUserEnabled : 更新用户启用状态
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param enabled 启用状态
     * @param ids 用户ID集合
     */
    void updateUserEnabled(@Param("enabled") Integer enabled, @Param("ids") List<Long> ids);
}