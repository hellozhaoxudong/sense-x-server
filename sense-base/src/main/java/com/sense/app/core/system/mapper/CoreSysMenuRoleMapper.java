package com.sense.app.core.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.sense.app.core.system.domain.CoreSysMenu;
import com.sense.app.core.system.domain.CoreSysMenuRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统菜单-角色分配Mapper（需要租户隔离）
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface CoreSysMenuRoleMapper extends MPJBaseMapper<CoreSysMenuRole> {

    /**
     * 查询角色下分配的菜单信息
     */
    List<CoreSysMenu> queryMenuByRoleId(Long roleId);
}
