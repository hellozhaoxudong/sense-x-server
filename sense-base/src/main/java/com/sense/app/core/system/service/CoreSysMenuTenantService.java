package com.sense.app.core.system.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.exception.BizException;
import com.sense.app.core.system.domain.CoreSysMenu;
import com.sense.app.core.system.domain.CoreSysMenuRole;
import com.sense.app.core.system.domain.CoreSysMenuTenant;
import com.sense.app.core.system.mapper.CoreSysMenuMapper;
import com.sense.app.core.system.mapper.CoreSysMenuTenantMapper;
import com.sense.app.core.user.domain.SysRole;
import com.sense.app.core.user.mapper.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统菜单-租户分配Service
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreSysMenuTenantService extends ServiceImpl<CoreSysMenuTenantMapper, CoreSysMenuTenant> {

    @Autowired
    private CoreSysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private CoreSysMenuRoleService menuRoleService;

    /**
     * 分页查询租户菜单分配列表
     * @param mybatisPage 分页参数
     * @param tenantId 租户ID
     * @return 分配列表
     */
    public List<CoreSysMenuTenant> queryMenuTenantList(Page mybatisPage, Long tenantId) {
        LambdaQueryWrapper<CoreSysMenuTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(tenantId != null, CoreSysMenuTenant::getTenantId, tenantId)
                   .orderByDesc(CoreSysMenuTenant::getCreateDate);
        
        Page<CoreSysMenuTenant> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }

    /**
     * 根据租户ID查询已分配的菜单ID列表
     * @param tenantId 租户ID
     * @return 菜单ID列表
     */
    public List<Long> queryMenuIdsByTenantId(Long tenantId) {
        LambdaQueryWrapper<CoreSysMenuTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CoreSysMenuTenant::getMenuId)
                   .eq(CoreSysMenuTenant::getTenantId, tenantId);
        return list(queryWrapper).stream()
                .map(CoreSysMenuTenant::getMenuId)
                .collect(Collectors.toList());
    }

    /**
     * 根据菜单ID查询已分配的租户ID列表
     * @param menuId 菜单ID
     * @return 租户ID列表
     */
    public List<Long> queryTenantIdsByMenuId(Long menuId) {
        LambdaQueryWrapper<CoreSysMenuTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CoreSysMenuTenant::getTenantId)
                   .eq(CoreSysMenuTenant::getMenuId, menuId);
        return list(queryWrapper).stream()
                .map(CoreSysMenuTenant::getTenantId)
                .collect(Collectors.toList());
    }

    /**
     * 为租户分配菜单权限
     * 分配时自动从菜单表查询菜单名称赋值给菜单别名
     * @param tenantId 租户ID（前端传入，非当前登录用户的租户）
     * @param menuIds 菜单ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignMenusToTenant(Long tenantId, List<Long> menuIds) {
        // 查询租户的租户管理员
        SysRole tenantAdmin = roleMapper.querySysRoleByCode("TenantAdmin", tenantId);
        if (null == tenantAdmin){
            throw new BizException("当前租户无租户管理员，请联系厂商～");
        }

        // 先删除该租户的所有菜单分配
        remove(new LambdaQueryWrapper<CoreSysMenuTenant>().eq(CoreSysMenuTenant::getTenantId, tenantId));
        // 删除该租户管理员下的菜单
        menuRoleService.remove(new LambdaQueryWrapper<CoreSysMenuRole>().eq(CoreSysMenuRole::getRoleId, tenantAdmin.getId()));

        if (CollUtil.isEmpty(menuIds)) {
            return;
        }

        // 查询菜单表获取菜单名称，用于赋值给菜单别名
        List<CoreSysMenu> menus = menuMapper.selectByIds(menuIds);

        // 批量插入新的菜单分配，菜单别名默认等于菜单名称
        List<CoreSysMenuTenant> menuTenantList = new ArrayList<>();
        List<CoreSysMenuRole> menuRoleList = new ArrayList<>();

        for (CoreSysMenu menu : menus) {
            menuTenantList.add(CoreSysMenuTenant.builder()
                    .menuId(menu.getId()).tenantId(tenantId).menuAliasName(menu.getMenuName()).build());

            menuRoleList.add(CoreSysMenuRole.builder()
                    .menuId(menu.getId()).roleId(tenantAdmin.getId()).tenantId(tenantId).menuAliasName(menu.getMenuName()).build());
        }

        // 租户分配菜单
        saveBatch(menuTenantList);

        // 为租户管理员设置菜单权限
        menuRoleService.saveBatch(menuRoleList);
    }

    /**
     * 批量为多个租户分配相同的菜单权限
     * @param tenantIds 租户ID列表
     * @param menuIds 菜单ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignMenusToTenants(List<Long> tenantIds, List<Long> menuIds) {
        if (CollUtil.isEmpty(tenantIds)) {
            throw new IllegalArgumentException("租户ID列表不能为空");
        }
        
        if (CollUtil.isEmpty(menuIds)) {
            throw new IllegalArgumentException("菜单ID列表不能为空");
        }
        
        // 为每个租户分配菜单
        for (Long tenantId : tenantIds) {
            assignMenusToTenant(tenantId, menuIds);
        }
        
        log.info("为 {} 个租户分配了 {} 个菜单权限", tenantIds.size(), menuIds.size());
    }

    /**
     * 从租户移除指定菜单权限
     * @param tenantId 租户ID
     * @param menuIds 菜单ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeMenusFromTenant(Long tenantId, List<Long> menuIds) {
        if (tenantId == null || CollUtil.isEmpty(menuIds)) {
            return;
        }
        
        LambdaQueryWrapper<CoreSysMenuTenant> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CoreSysMenuTenant::getTenantId, tenantId)
                    .in(CoreSysMenuTenant::getMenuId, menuIds);
        remove(deleteWrapper);
        
        log.info("从租户 {} 移除了 {} 个菜单权限", tenantId, menuIds.size());
    }

    /**
     * 清空租户的所有菜单权限
     * @param tenantId 租户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearTenantMenus(Long tenantId) {
        if (tenantId == null) {
            return;
        }
        
        LambdaQueryWrapper<CoreSysMenuTenant> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CoreSysMenuTenant::getTenantId, tenantId);
        remove(deleteWrapper);
        
        log.info("清空了租户 {} 的所有菜单权限", tenantId);
    }

    /**
     * 查询租户已分配的菜单树形结构（包含菜单别名）
     * 内存中组装树形结构，不写复杂SQL
     * @param tenantId 租户ID
     * @return 菜单树形结构（带别名）
     */
    public List<CoreSysMenu> queryTenantMenuTree(Long tenantId) {
        // 1. 查询该租户已分配的菜单关联记录
        LambdaQueryWrapper<CoreSysMenuTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoreSysMenuTenant::getTenantId, tenantId);
        List<CoreSysMenuTenant> tenantMenus = list(queryWrapper);

        if (CollUtil.isEmpty(tenantMenus)) {
            return new ArrayList<>();
        }

        // 2. 提取菜单ID和别名Map
        List<Long> menuIds = tenantMenus.stream().map(CoreSysMenuTenant::getMenuId).collect(Collectors.toList());
        Map<Long, String> aliasMap = tenantMenus.stream()
                .collect(Collectors.toMap(CoreSysMenuTenant::getMenuId, 
                        mt -> mt.getMenuAliasName() != null ? mt.getMenuAliasName() : ""));

        // 3. 查询菜单表获取菜单详细信息
        List<CoreSysMenu> menus = menuMapper.selectByIds(menuIds);

        // 4. 设置菜单别名
        for (CoreSysMenu menu : menus) {
            menu.setMenuAliasName(aliasMap.getOrDefault(menu.getId(), menu.getMenuName()));
        }

        // 5. 在内存中构建树形结构
        return buildTree(menus);
    }

    /**
     * 批量更新菜单别名
     * @param tenantId 租户ID
     * @param aliasList 别名列表（menuId + menuAliasName）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMenuAlias(Long tenantId, List<CoreSysMenuTenant> aliasList) {
        if (tenantId == null || CollUtil.isEmpty(aliasList)) {
            return;
        }

        for (CoreSysMenuTenant item : aliasList) {
            LambdaQueryWrapper<CoreSysMenuTenant> updateWrapper = new LambdaQueryWrapper<>();
            updateWrapper.eq(CoreSysMenuTenant::getTenantId, tenantId)
                        .eq(CoreSysMenuTenant::getMenuId, item.getMenuId());
            CoreSysMenuTenant entity = getOne(updateWrapper);
            if (entity != null) {
                entity.setMenuAliasName(item.getMenuAliasName());
                updateById(entity);
            }
        }

        log.info("更新租户 {} 的 {} 个菜单别名", tenantId, aliasList.size());
    }

    /**
     * 将平铺菜单列表构建为树形结构
     */
    private List<CoreSysMenu> buildTree(List<CoreSysMenu> allMenus) {
        // 找到所有顶级菜单（parentId == 0 或 父节点不在已分配列表中）
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
