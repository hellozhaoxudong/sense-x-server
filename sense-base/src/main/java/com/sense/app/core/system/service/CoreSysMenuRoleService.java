package com.sense.app.core.system.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.system.domain.CoreSysMenu;
import com.sense.app.core.system.domain.CoreSysMenuRole;
import com.sense.app.core.system.domain.CoreSysMenuTenant;
import com.sense.app.core.system.mapper.CoreSysMenuRoleMapper;
import com.sense.app.core.system.mapper.CoreSysMenuTenantMapper;
import com.sense.app.core.utils.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统菜单-角色分配Service
 * 租户管理员操作，为租户内的角色分配菜单权限
 * 
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreSysMenuRoleService extends ServiceImpl<CoreSysMenuRoleMapper, CoreSysMenuRole> {

    @Autowired
    private CoreSysMenuService menuService;

    @Autowired
    private CoreSysMenuTenantMapper menuTenantMapper;

    /**
     * 分页查询角色菜单分配列表
     * @param mybatisPage 分页参数
     * @param roleId 角色ID
     * @return 分配列表
     */
    public List<CoreSysMenuRole> queryMenuRoleList(Page mybatisPage, Long roleId) {
        LambdaQueryWrapper<CoreSysMenuRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(roleId != null, CoreSysMenuRole::getRoleId, roleId)
                   .orderByDesc(CoreSysMenuRole::getCreateDate);
        
        Page<CoreSysMenuRole> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }

    /**
     * 根据角色ID查询已分配的菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    public List<Long> queryMenuIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<CoreSysMenuRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CoreSysMenuRole::getMenuId)
                   .eq(CoreSysMenuRole::getRoleId, roleId);
        return list(queryWrapper).stream()
                .map(CoreSysMenuRole::getMenuId)
                .collect(Collectors.toList());
    }

    /**
     * 根据菜单ID查询已分配的角色ID列表
     * @param menuId 菜单ID
     * @return 角色ID列表
     */
    public List<Long> queryRoleIdsByMenuId(Long menuId) {
        LambdaQueryWrapper<CoreSysMenuRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CoreSysMenuRole::getRoleId)
                   .eq(CoreSysMenuRole::getMenuId, menuId);
        return list(queryWrapper).stream()
                .map(CoreSysMenuRole::getRoleId)
                .collect(Collectors.toList());
    }

    /**
     * 为角色分配菜单权限
     * 分配时自动从租户菜单分配表中获取别名
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignMenusToRole(Long roleId, List<Long> menuIds) {
        if (roleId == null) {
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        Long tenantId = CurrentUser.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("未获取到当前租户信息");
        }
        
        // 先删除该角色的所有菜单分配
        LambdaQueryWrapper<CoreSysMenuRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CoreSysMenuRole::getRoleId, roleId);
        remove(deleteWrapper);
        
        // 如果菜单列表为空，则直接返回（相当于清空该角色的所有菜单权限）
        if (CollUtil.isEmpty(menuIds)) {
            log.info("清空角色 {} 的所有菜单权限", roleId);
            return;
        }

        // 从租户菜单分配表获取别名
        LambdaQueryWrapper<CoreSysMenuTenant> tenantQuery = new LambdaQueryWrapper<>();
        tenantQuery.eq(CoreSysMenuTenant::getTenantId, tenantId)
                   .in(CoreSysMenuTenant::getMenuId, menuIds);
        List<CoreSysMenuTenant> tenantMenus = menuTenantMapper.selectList(tenantQuery);
        Map<Long, String> aliasMap = tenantMenus.stream()
                .collect(Collectors.toMap(CoreSysMenuTenant::getMenuId,
                        mt -> mt.getMenuAliasName() != null ? mt.getMenuAliasName() : ""));
        
        // 批量插入新的菜单分配，别名默认继承租户菜单别名
        List<CoreSysMenuRole> menuRoleList = menuIds.stream()
                .map(menuId -> CoreSysMenuRole.builder()
                        .menuId(menuId)
                        .roleId(roleId)
                        .tenantId(tenantId)
                        .menuAliasName(aliasMap.getOrDefault(menuId, ""))
                        .build())
                .collect(Collectors.toList());
        
        saveBatch(menuRoleList);
        log.info("为角色 {} 分配了 {} 个菜单权限", roleId, menuIds.size());
    }

    /**
     * 批量为多个角色分配相同的菜单权限
     * @param roleIds 角色ID列表
     * @param menuIds 菜单ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignMenusToRoles(List<Long> roleIds, List<Long> menuIds) {
        if (CollUtil.isEmpty(roleIds)) {
            throw new IllegalArgumentException("角色ID列表不能为空");
        }
        
        if (CollUtil.isEmpty(menuIds)) {
            throw new IllegalArgumentException("菜单ID列表不能为空");
        }
        
        Long tenantId = CurrentUser.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("未获取到当前租户信息");
        }
        
        // 为每个角色分配菜单
        for (Long roleId : roleIds) {
            assignMenusToRole(roleId, menuIds);
        }
        
        log.info("为 {} 个角色分配了 {} 个菜单权限", roleIds.size(), menuIds.size());
    }

    /**
     * 从角色移除指定菜单权限
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeMenusFromRole(Long roleId, List<Long> menuIds) {
        if (roleId == null || CollUtil.isEmpty(menuIds)) {
            return;
        }
        
        LambdaQueryWrapper<CoreSysMenuRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CoreSysMenuRole::getRoleId, roleId)
                    .in(CoreSysMenuRole::getMenuId, menuIds);
        remove(deleteWrapper);
        
        log.info("从角色 {} 移除了 {} 个菜单权限", roleId, menuIds.size());
    }

    /**
     * 清空角色的所有菜单权限
     * @param roleId 角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearRoleMenus(Long roleId) {
        if (roleId == null) {
            return;
        }
        
        LambdaQueryWrapper<CoreSysMenuRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CoreSysMenuRole::getRoleId, roleId);
        remove(deleteWrapper);
        
        log.info("清空了角色 {} 的所有菜单权限", roleId);
    }

    /**
     * 查询角色已分配的菜单树形结构（包含菜单别名）
     * @param roleId 角色ID
     * @return 菜单树形结构（带别名）
     */
    public List<CoreSysMenu> queryRoleMenuTree(Long roleId) {
        // 1. 查询该角色已分配的菜单关联记录
        LambdaQueryWrapper<CoreSysMenuRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoreSysMenuRole::getRoleId, roleId);
        List<CoreSysMenuRole> roleMenus = list(queryWrapper);

        if (CollUtil.isEmpty(roleMenus)) {
            return new ArrayList<>();
        }

        // 2. 提取菜单ID和别名Map
        List<Long> menuIds = roleMenus.stream().map(CoreSysMenuRole::getMenuId).collect(Collectors.toList());
        Map<Long, String> aliasMap = roleMenus.stream()
                .collect(Collectors.toMap(CoreSysMenuRole::getMenuId,
                        mr -> mr.getMenuAliasName() != null ? mr.getMenuAliasName() : ""));

        // 3. 查询菜单表获取菜单详细信息
        List<CoreSysMenu> menus = menuService.listByIds(menuIds);

        // 4. 设置菜单别名
        for (CoreSysMenu menu : menus) {
            menu.setMenuAliasName(aliasMap.getOrDefault(menu.getId(), menu.getMenuName()));
        }

        // 5. 在内存中构建树形结构
        return buildTree(menus);
    }

    /**
     * 批量更新角色菜单别名
     * @param roleId 角色ID
     * @param aliasList 别名列表（menuId + menuAliasName）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenuAlias(Long roleId, List<CoreSysMenuRole> aliasList) {
        if (roleId == null || CollUtil.isEmpty(aliasList)) {
            return;
        }

        for (CoreSysMenuRole item : aliasList) {
            LambdaQueryWrapper<CoreSysMenuRole> updateWrapper = new LambdaQueryWrapper<>();
            updateWrapper.eq(CoreSysMenuRole::getRoleId, roleId)
                        .eq(CoreSysMenuRole::getMenuId, item.getMenuId());
            CoreSysMenuRole entity = getOne(updateWrapper);
            if (entity != null) {
                entity.setMenuAliasName(item.getMenuAliasName());
                updateById(entity);
            }
        }

        log.info("更新角色 {} 的 {} 个菜单别名", roleId, aliasList.size());
    }

    /**
     * 将平铺菜单列表构建为树形结构
     */
    private List<CoreSysMenu> buildTree(List<CoreSysMenu> allMenus) {
        List<Long> allIds = allMenus.stream().map(CoreSysMenu::getId).collect(Collectors.toList());
        List<CoreSysMenu> rootMenus = allMenus.stream()
                .filter(m -> m.getParentId().equals(0L) || !allIds.contains(m.getParentId()))
                .collect(Collectors.toList());

        for (CoreSysMenu rootMenu : rootMenus) {
            buildTreeRecursively(rootMenu, allMenus);
        }

        return rootMenus;
    }

    /**
     * 递归构建菜单树
     */
    private void buildTreeRecursively(CoreSysMenu parentMenu, List<CoreSysMenu> allMenus) {
        for (CoreSysMenu menu : allMenus) {
            if (menu.getParentId().equals(parentMenu.getId())) {
                if (parentMenu.getChildren() == null) {
                    parentMenu.setChildren(new ArrayList<>());
                }
                parentMenu.getChildren().add(menu);
                buildTreeRecursively(menu, allMenus);
            }
        }
    }
}
