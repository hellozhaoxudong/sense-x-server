package com.sense.app.core.user.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.core.user.domain.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName SysUserRoleMapper
 * @description 用户角色分配管理Mapper
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * queryUserRoles : 查询用户的角色信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param userIds 用户ID集合
     */
    public List<SysUserRole> queryUserRoles(@Param("userIds") List<Long> userIds);
}
