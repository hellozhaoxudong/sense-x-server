package com.sense.app.core.user.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.exception.BizException;
import com.sense.app.core.system.mapper.CoreSysMenuMapper;
import com.sense.app.core.user.domain.*;
import com.sense.app.core.user.mapper.*;
import com.sense.app.core.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName SysTenantService
 * @description 租户管理Service
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysTenantService extends ServiceImpl<SysTenantMapper, SysTenant> {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserTenantMapper sysUserTenantMapper;

    @Autowired
    private CoreSysMenuMapper sysMenuMapper;

    /**
     * queryTenants : 查询租户列表信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param mybatisPage   分页信息
     * @param tenantName	租户名称
     * @param companyName	公司名称
     * @param enabled	启用状态
     */
    public List<SysTenant> queryTenants(Page mybatisPage, String tenantName, String companyName, Integer enabled){

        Page<SysTenant> results = page(mybatisPage,new QueryWrapper<SysTenant>()
                .like(StrUtil.isNotEmpty(tenantName),"TENANT_NAME", tenantName)
                .like(StrUtil.isNotEmpty(companyName),"COMPANY_NAME", companyName)
                .eq(null != enabled,"ENABLED", enabled));

        return results.getRecords();
    }

    /**
     * 查询租户管理员信息
     * @param tenantId  租户ID
     * @return
     */
    public SysUser queryTenantAdminUser(Long tenantId){
        // 查询租户下管理员角色
        SysRole role = sysRoleMapper.querySysRoleByCode("TenantAdmin", tenantId);
        if (null == role){
            throw new BizException("当前租户下无租户管理员角色");
        }

        // 查询用户
        SysUserRole userRole = sysUserRoleMapper.selectOne(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, role.getId()).eq(SysUserRole::getTenantId, tenantId));
        if (null == userRole){
            throw new BizException("当前租户下无租户管理员用户");
        }

        SysUser user = sysUserMapper.selectById(userRole.getUserId());
        user.setPassword(null);
        return user;
    }

    /**
     * submitTenant : 提交租户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param data	租户信息
     */
    public void submitTenant(SysTenant data){
        saveOrUpdate(data);
    }

    /**
     * 创建一个新租户
     * @param tenant
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    public void createTenant(SysTenant tenant){
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getTenantCode, tenant.getTenantCode()));
        if (count>0){
            throw new BizException("租户编码已被使用，请更换");
        }

        // 保存租户信息
        save(tenant);

        // 初始化租户管理员角色
        SysRole role = SysRole.builder().id(IdWorker.getId())
                .roleCode("TenantAdmin")
                .roleName(tenant.getTenantName() + "-租户管理员")
                .tenantId(tenant.getId()).build();
        sysRoleMapper.insert(role);

        // 初始化用户信息
        SysUser sysUser = tenant.getAdminUser();
        Long loginNameCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername()));
        if (loginNameCount>0){
            throw new BizException("登陆名已被使用，请更换");
        }
        Long phoneCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, sysUser.getPhone()));
        if (phoneCount>0){
            throw new BizException("手机号已被使用，请更换");
        }

        // 保存用户
        sysUser.setName("租户管理员");
        sysUser.setLocked("N");
        sysUser.setEnabled("Y");
        sysUser.setPassword(PasswordUtils.encryptPassword(sysUser.getPassword()));
        sysUserMapper.insert(sysUser);

        // 给用户分配租户
        SysUserTenant userTenant = SysUserTenant.builder().userId(sysUser.getId())
                .tenantId(tenant.getId())
                .mainTenant(1)
                .build();
        sysUserTenantMapper.insert(userTenant);

        // 给用户分配角色
        SysUserRole userRole = SysUserRole.builder().roleId(role.getId())
                .userId(sysUser.getId())
                .tenantId(tenant.getId()).build();
        sysUserRoleMapper.insert(userRole);
    }

    /**
     * deleteTenants : 删除租户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids	租户ID集合
     */
    public void deleteTenants(List<Long> ids){
        removeByIds(ids);
    }
}
