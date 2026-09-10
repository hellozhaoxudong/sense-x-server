package com.sense.app.core.user.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.core.user.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName SysRoleMapper
 * @description 角色管理Mapper
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * queryUserRoleList : 查询某用户在某租户下的角色列表
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param userId	用户ID
     * @param tenantId	租户ID
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysRole> queryUserRoleList(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    /**
     * querySysRoleByCode : 查询某租户下某角色
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param roleCode	角色编码
     * @param tenantId	租户编码
     */
    @InterceptorIgnore(tenantLine = "true")
    SysRole querySysRoleByCode(@Param("roleCode") String roleCode, @Param("tenantId") Long tenantId);

    /**
     * querySysRoleIgnoreTenant : 忽略租户查询角色信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids 角色ID集合
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysRole> querySysRoleIgnoreTenant(@Param("ids") List<Long> ids);
}
