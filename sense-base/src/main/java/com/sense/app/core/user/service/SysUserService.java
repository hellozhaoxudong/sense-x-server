package com.sense.app.core.user.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.exception.NoAuthException;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.domain.SysUserRole;
import com.sense.app.core.user.domain.SysUserTenant;
import com.sense.app.core.user.mapper.*;
import com.sense.app.core.utils.CurrentUser;
import com.sense.app.core.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SysUserService
 * @description 用户管理Service
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    @Autowired
    private SysUserTenantMapper sysUserTenantMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysTenantMapper tenantMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 查询当前租户下用户简易信息
     * @param filter        筛选条件
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    public List<SysUser> querySimpleSysUsers(String filter){
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>().select(SysUser::getId, SysUser::getUsername, SysUser::getName);
        if (StrUtil.isNotEmpty(filter)){
            wrapper.like(SysUser::getName, filter).or().like(SysUser::getUsername, filter)
                    .or().like(SysUser::getEmail, filter)
                    .or().like(SysUser::getPhone, filter);
        }

        return list(wrapper);
    }

    /**
     * 查询当前租户下的所有用户简易信息(区分租户)
     */
    public List<SysUser> querySimpleSysUsersInTenant() {
        Long tenantId = CurrentUser.getTenantId();
        if (null == tenantId) {
            return new ArrayList<>();
        }
        return baseMapper.queryUsers(new Page<>(-1,-1), tenantId, null, null, null, null);
    }

    /**
     * queryUsers : 查询当前租户下的所有用户
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param mybatisPage   分页信息
     * @param loginname     登录名
     * @param name          用户名
     * @param phone         电话
     * @param enabled       启用状态
     */
    public List<SysUser> queryUsers(Page mybatisPage, String loginname, String name, String phone, Integer enabled){
        Long tenantId = CurrentUser.getTenantId();
        if(null == tenantId){
            return new ArrayList<>();
        }

        // 获取用户信息
        List<SysUser> users = baseMapper.queryUsers(mybatisPage, tenantId, loginname, name, phone, enabled);
        if(CollUtil.isEmpty(users)){
            return users;
        }

        // 设置角色
        List<Long> userIds = users.stream().map(SysUser::getId).collect(Collectors.toList());
        List<SysUserRole> userRoles = sysUserRoleMapper.queryUserRoles(userIds);
        if(CollUtil.isEmpty(userRoles)){
            return users;
        }

        // 按用户ID分组
        Map<Long, List<SysUserRole>> userRoleMap = userRoles.stream().collect(Collectors.groupingBy(SysUserRole::getUserId));
        for(SysUser user : users){
            // 获取该用户所有角色名称、角色ID
            List<SysUserRole> roles = userRoleMap.getOrDefault(user.getId(), new ArrayList<>());
            String roleNames = roles.stream().map(SysUserRole::getRoleName).collect(Collectors.joining(", "));
            List<Long> roleIds = roles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

            user.setRoleNames(roleNames);
            user.setRoleIds(roleIds);
        }

        return users;
    }

    /**
     * submitSysUser : 提交用户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param sysUser 用户信息
     */
    public void submitSysUser(SysUser sysUser){
        if(null == sysUser.getId()){
            insertSysUser(sysUser);
        }else{
            // 更新时只更新基础信息，不再处理角色
            if (StrUtil.isNotEmpty(sysUser.getPassword())) {
                sysUser.setPassword(PasswordUtils.encryptPassword(sysUser.getPassword()));
            }

            baseMapper.updateById(sysUser);
        }
    }

    /**
     * 修改我本人的基础信息
     */
    public void changeMyInfo(SysUser sysUser){
        sysUser.setId(CurrentUser.getUserId());
        updateById(sysUser);

        // 更新当前登录信息
        SysUser userCache = CurrentUser.getUser();
        userCache.setUsername(sysUser.getUsername());
        userCache.setName(sysUser.getName());
        userCache.setPhone(sysUser.getPhone());
        userCache.setEmail(sysUser.getEmail());

        StpUtil.getTokenSession().set("user", JSONUtil.toJsonStr(userCache));
    }

    /**
     * 修改我本人的密码
     */
    public void changeMyPassword(String oldPassword, String newPassword){
        // 验证旧密码填写是否正确
        SysUser user = baseMapper.selectById(CurrentUser.getUserId());
        if (!user.getPassword().equals(PasswordUtils.encryptPassword(oldPassword))){
            throw new NoAuthException("旧密码填写错误！");
        }

        // 更新密码
        user.setPassword(PasswordUtils.encryptPassword(newPassword));
        baseMapper.updateById(user);
    }

    /**
     * insertSysUser : 新增用户
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param sysUser	用户信息
     */
    public void insertSysUser(SysUser sysUser){
        // 设置默认信息
        sysUser.setId(IdWorker.getId());
        sysUser.setPassword(PasswordUtils.encryptPassword(sysUser.getPassword()));
        sysUser.setLocked("N");
        sysUser.setEnabled("Y");
        baseMapper.insert(sysUser);

        // 给用户分配租户
        Long tenantId = CurrentUser.getTenantId();
        SysUserTenant sysUserTenant = new SysUserTenant();
        sysUserTenant.setTenantId(tenantId);
        sysUserTenant.setUserId(sysUser.getId());
        sysUserTenantMapper.insert(sysUserTenant);

        // 给用户分配角色
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(sysUser.getId());
        sysUserRole.setTenantId(tenantId);
        for(Long roleId : sysUser.getRoleIds()){
            sysUserRole.setId(IdWorker.getId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleMapper.insert(sysUserRole);
        }
    }

    /**
     * deleteSysUser : 删除用户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids 用户IDS
     */
    public void deleteSysUser(List<Long> ids){
        Long tenantId = CurrentUser.getTenantId();

        // 删除租户-用户分配信息
        sysUserTenantMapper.delete(new QueryWrapper<SysUserTenant>().in("user_id", ids).eq("tenant_id", tenantId));

        // 删除角色-用户分配信息
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().in("user_id", ids).eq("tenant_id", tenantId));

        // 如果该用户还入驻了别的租户，不删除用户信息
        for(Long userId : ids){
            long count = sysUserTenantMapper.selectCount(new QueryWrapper<SysUserTenant>().eq("user_id", userId).eq("tenant_id", tenantId));
            if(count>0){
                continue;
            }
            baseMapper.deleteById(userId);
        }
    }

    /**
     * enableSysUser : 启用用户
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids 用户id集合
     */
    public void enableSysUser(List<Long> ids){
        baseMapper.updateUserEnabled(1, ids);
    }

    /**
     * disableSysUser : 禁用用户
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids	用户id集合
     */
    public void disableSysUser(List<Long> ids){
        baseMapper.updateUserEnabled(0, ids);
    }

    /**
     * distributeRoles : 给用户分配角色
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param userId	用户ID
     * @param roleIds	角色ID集合
     */
    public void distributeRoles(Long userId, List<Long> roleIds){
        // 删除旧数据
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id", userId));

        // 分配角色
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setTenantId(CurrentUser.getTenantId());
        for(Long roleId : roleIds){
            userRole.setId(IdWorker.getId());
            userRole.setRoleId(roleId);
            sysUserRoleMapper.insert(userRole);
        }
    }
}
