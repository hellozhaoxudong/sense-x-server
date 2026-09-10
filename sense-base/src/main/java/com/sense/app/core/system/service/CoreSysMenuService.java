package com.sense.app.core.system.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sense.app.core.system.domain.CoreSysApp;
import com.sense.app.core.system.domain.CoreSysMenu;
import com.sense.app.core.system.domain.CoreSysMenuTenant;
import com.sense.app.core.system.mapper.CoreSysMenuMapper;
import com.sense.app.core.system.mapper.CoreSysMenuTenantMapper;
import com.sense.app.core.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CoreSysMenuService extends ServiceImpl<CoreSysMenuMapper, CoreSysMenu> {

    @Autowired
    private CoreSysMenuTenantMapper menuTenantMapper;

    /**
     * 查询所有菜单并构建树形结构
     * @return 菜单树
     */
    public List<CoreSysMenu> queryMenuTree(Long appId) {
        List<CoreSysMenu> allMenus = baseMapper.selectList(new LambdaQueryWrapper<CoreSysMenu>()
                .eq(CoreSysMenu::getAppId, appId)
                .orderByAsc(CoreSysMenu::getSortOrder));
        return buildTree(allMenus);
    }

    /**
     * 查询所有菜单（平铺列表，不分页）
     * @return 菜单列表
     */
    public List<CoreSysMenu> queryAllMenus() {
        LambdaQueryWrapper<CoreSysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(CoreSysMenu::getSortOrder)
                   .orderByAsc(CoreSysMenu::getId);
        return list(queryWrapper);
    }

    /**
     * 查询所有菜单树
     * 用户为租户分配菜单时的选择
     */
    public List<CoreSysMenu> queryAllMenuTree(){
        // 查询所有菜单
        List<CoreSysMenu> menus = baseMapper.selectJoinList(new MPJLambdaWrapper<CoreSysMenu>()
                .selectAll(CoreSysMenu.class)
                .selectAs(CoreSysApp::getAppName, CoreSysMenu::getAppName)
                .leftJoin(CoreSysApp.class, CoreSysApp::getId, CoreSysMenu::getAppId)
                .orderByAsc(CoreSysApp::getSortOrder)
                .orderByAsc(CoreSysMenu::getSortOrder)
                .orderByAsc(CoreSysMenu::getId)
        );

        return buildTree(menus);
    }

    /**
     * 查询租户内菜单树
     * 用户为角色分配菜单时的选择
     */
    public List<CoreSysMenu> queryTenantMenuTree(){
        // 查询本租户内被分配了哪些菜单
        List<CoreSysMenuTenant> menuTenants = menuTenantMapper.selectList(new LambdaQueryWrapper<CoreSysMenuTenant>().eq(CoreSysMenuTenant::getTenantId, CurrentUser.getTenantId()));
        if (CollUtil.isEmpty(menuTenants)) {
            return new ArrayList<>();
        }
        // 菜单ID
        List<Long> menuIds = menuTenants.stream().map(CoreSysMenuTenant::getMenuId).toList();
        // Map<菜单ID、菜单别名>
        Map<Long, String> menuNameMap = menuTenants.stream().collect(Collectors.toMap(CoreSysMenuTenant::getMenuId, CoreSysMenuTenant::getMenuAliasName));


        // 查询菜单
        List<CoreSysMenu> menus = baseMapper.selectJoinList(new MPJLambdaWrapper<CoreSysMenu>()
                .selectAll(CoreSysMenu.class)
                .selectAs(CoreSysApp::getAppName, CoreSysMenu::getAppName)
                .leftJoin(CoreSysApp.class, CoreSysApp::getId, CoreSysMenu::getAppId)
                .in(CoreSysMenu::getId, menuIds)
                .orderByAsc(CoreSysApp::getSortOrder)
                .orderByAsc(CoreSysMenu::getSortOrder)
                .orderByAsc(CoreSysMenu::getId)
        );

        menus.forEach(menu -> {
            menu.setMenuAliasName(menuNameMap.get(menu.getId()));
        });

        return buildTree(menus);
    }

    /**
     * 根据父菜单ID查询子菜单
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    public List<CoreSysMenu> queryByParentId(Long parentId) {
        LambdaQueryWrapper<CoreSysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoreSysMenu::getParentId, parentId)
                   .orderByAsc(CoreSysMenu::getSortOrder)
                   .orderByAsc(CoreSysMenu::getId);
        return list(queryWrapper);
    }

    /**
     * 将平铺菜单列表构建为树形结构
     * @param allMenus 所有菜单
     * @return 树形菜单列表（仅包含顶级节点）
     */
    public List<CoreSysMenu> buildTree(List<CoreSysMenu> allMenus) {
        // 找到所有顶级菜单
        List<CoreSysMenu> rootMenus = allMenus.stream()
                .filter(m -> null==m.getParentId() || m.getParentId().equals(0L))
                .collect(Collectors.toList());

        // 为每个顶级菜单递归构建子树
        for (CoreSysMenu rootMenu : rootMenus) {
            buildTreeRecursively(rootMenu, allMenus);
        }

        return rootMenus;
    }

    /**
     * 递归构建菜单树
     * @param parentMenu 父菜单
     * @param allMenus 所有菜单列表
     * @return 父菜单（包含子菜单）
     */
    private CoreSysMenu buildTreeRecursively(CoreSysMenu parentMenu, List<CoreSysMenu> allMenus) {
        for (CoreSysMenu menu : allMenus) {
            if (menu.getParentId().equals(parentMenu.getId())) {
                if (parentMenu.getChildren() == null) {
                    parentMenu.setChildren(new ArrayList<>());
                }
                parentMenu.getChildren().add(menu);
                // 递归构建子菜单的子菜单
                buildTreeRecursively(menu, allMenus);
            }
        }
        return parentMenu;
    }
}
