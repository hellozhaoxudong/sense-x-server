package com.sense.app.core.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.sense.app.core.system.domain.CoreSysMenuTenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统菜单-租户分配Mapper（忽略租户隔离）
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface CoreSysMenuTenantMapper extends MPJBaseMapper<CoreSysMenuTenant> {
}
