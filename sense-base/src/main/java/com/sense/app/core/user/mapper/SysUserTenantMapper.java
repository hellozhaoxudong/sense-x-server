package com.sense.app.core.user.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.core.user.domain.SysUserTenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName SysUserTenantMapper
 * @description 用户租户分配Mapper（忽略租户）
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface SysUserTenantMapper extends BaseMapper<SysUserTenant> {

    /**
     * queryUserTenants : 查询用户租户关系信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param userIds 用户ID集合
     */
    List<SysUserTenant> queryUserTenants(@Param("userIds") List<Long> userIds);
}
