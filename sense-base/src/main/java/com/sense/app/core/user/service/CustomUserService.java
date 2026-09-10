package com.sense.app.core.user.service;

import cn.hutool.core.lang.Validator;
import com.sense.app.core.exception.NoAuthException;
import com.sense.app.core.system.domain.CoreSysApp;
import com.sense.app.core.system.domain.CoreSysMenu;
import com.sense.app.core.system.mapper.CoreSysMenuRoleMapper;
import com.sense.app.core.system.service.CoreSysAppService;
import com.sense.app.core.system.service.CoreSysMenuService;
import com.sense.app.core.user.domain.SysRole;
import com.sense.app.core.user.domain.SysTenant;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.mapper.SysRoleMapper;
import com.sense.app.core.user.mapper.SysTenantMapper;
import com.sense.app.core.user.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class CustomUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysTenantMapper tenantMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private CoreSysMenuRoleMapper menuRoleMapper;

    @Autowired
    private CoreSysMenuService menuService;

    @Autowired
    private CoreSysAppService appService;

    /**
     * querySysUser : 查询用户信息，通过邮箱、手机号、登录名三种方式
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param loginName 邮箱/手机号/登录名
     */
    public SysUser querySysUser(String loginName){
        SysUser sysUser = null;

        if(Validator.isEmail(loginName)){
            // 尝试使用邮箱查询
            sysUser = sysUserMapper.queryUserByEmail(loginName);

        }else if(Validator.isMobile(loginName)){
            // 尝试使用手机号查询
            sysUser = sysUserMapper.queryUserByPhone(loginName);

        }else {
            // 尝试使用登录名查询
            sysUser = sysUserMapper.queryUserByLoginname(loginName);
        }

        return sysUser;
    }

    /**
     * 获取全量用户信息
     * @param sysUser
     * @return
     */
    public SysUser buildFullUserInfo(SysUser sysUser){
        // 去除敏感信息
        sysUser.setPassword(null);

        // 补充租户信息
        List<SysTenant> tenantList = tenantMapper.queryUserTenant(sysUser.getId());
        if(CollectionUtils.isEmpty(tenantList)){
            throw new NoAuthException("用户未分配租户，请联系管理员");
        }
        SysTenant tenant = tenantList.get(0);

        // 补充角色信息
        List<SysRole> roleList = roleMapper.queryUserRoleList(sysUser.getId(), tenant.getId());
        if(CollectionUtils.isEmpty(roleList)){
            throw new NoAuthException("用户未分配角色，请联系管理员");
        }
        SysRole role = roleList.get(0);

        // 查询本角色分配的菜单信息
        List<CoreSysMenu> menuList = menuRoleMapper.queryMenuByRoleId(role.getId());
        if(CollectionUtils.isEmpty(menuList)){
            throw new NoAuthException("本用户的角色："+ role.getRoleName() +" 未分配菜单，请联系管理员");
        }

        // 按应用分组
        Map<Long, List<CoreSysMenu>> appMenuList = menuList.stream().collect(Collectors.groupingBy(CoreSysMenu::getAppId));
        List<CoreSysApp> appList = appService.listByIds(appMenuList.keySet());

        // 填充应用下的菜单树
        for (CoreSysApp app : appList){
            app.setMenuList(menuService.buildTree(appMenuList.get(app.getId())));
        }

        sysUser.setTenantList(tenantList);
        sysUser.setTenant(tenant);
        sysUser.setTenantId(tenant.getId());
        sysUser.setRoleList(roleList);
        sysUser.setRole(role);
        sysUser.setAppList(appList);

        return sysUser;
    }
}
