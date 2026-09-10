package com.sense.app.core.user.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.excel.BigExcelUtil;
import com.sense.app.core.user.domain.SysRole;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.domain.SysUserRole;
import com.sense.app.core.user.mapper.SysRoleMapper;
import com.sense.app.core.user.mapper.SysUserMapper;
import com.sense.app.core.user.mapper.SysUserRoleMapper;
import com.sense.app.core.utils.CurrentUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName SysRoleService
 * @description 角色管理Service
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserService userService;

    /**
     * queryRoles : 查询角色信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param mybatisPage 分页信息
     */
    public List<SysRole> queryRoles(Page mybatisPage){

        Page<SysRole> results = page(mybatisPage,new LambdaQueryWrapper<SysRole>().ne(SysRole::getRoleCode, "TenantAdmin"));

        return results.getRecords();
    }

    /**
     * submitRole : 提交角色信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param data 角色信息
     */
    public void submitRole(SysRole data){
        saveOrUpdate(data);
    }

    /**
     * deleteRoles : 删除角色信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids 角色ID集合
     */
    public void deleteRoles(List<Long> ids){
        // 删除角色-用户分配信息
        userRoleMapper.delete(new QueryWrapper<SysUserRole>().in("role_id", ids));

        // 删除角色信息
        removeByIds(ids);
    }

    /**
     * 查询角色成员信息
     * @param roleId 角色ID
     */
    public List<SysUser> queryMembers(Long roleId) {

        // 查询当前角色下的用户ID
        List<Long> userIds = userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId).select(SysUserRole::getUserId))
                .stream().map(SysUserRole::getUserId).collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return new ArrayList<>();
        } else {
            return userMapper.selectList(new LambdaQueryWrapper<SysUser>()
                    .in(SysUser::getId, userIds)
                    .select(SysUser::getId, SysUser::getUsername, SysUser::getName, SysUser::getSex, SysUser::getPhone, SysUser::getEmail));
        }

    }

    /**
     * 查询角色中未添加的用户
     * @param roleId 角色ID
     */
    public List<SysUser> queryUnMembers(Long roleId) {

        // 查询当前租户下的所有用户
        List<SysUser> users = userService.querySimpleSysUsersInTenant();

        // 查询角色下的用户ID
        Set<Long> userIds = userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId))
                .stream().map(SysUserRole::getUserId).collect(Collectors.toSet());



        // 用户过滤
        return users.stream().filter(user -> !userIds.contains(user.getId())).collect(Collectors.toList());
    }

    /**
     * 批量添加角色成员
     * @param roleId 角色ID
     * @param userIds 成员ID集合
     */
    public void addMembers(Long roleId, List<Long> userIds) {
        for (Long userId : userIds) {
            SysUserRole userRole = SysUserRole.builder()
                    .id(IdWorker.getId())
                    .userId(userId)
                    .tenantId(CurrentUser.getTenantId())
                    .roleId(roleId).build();
            userRoleMapper.insert(userRole);
        }
    }

    /**
     * 批量删除角色成员
     * @param roleId 角色ID
     * @param userIds 成员ID集合
     */
    public void removeMembers(Long roleId, List<Long> userIds) {
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId)
                .in(SysUserRole::getUserId, userIds));
    }

    /**
     * 导出角色信息
     */
    public void exportRoles(HttpServletResponse response){
        // 查询所有角色信息
        List<SysRole> roles = baseMapper.selectList(new LambdaQueryWrapper<SysRole>().ne(SysRole::getRoleCode, "TenantAdmin"));

        // 表头
        List<Object> heads = CollUtil.list(true, "角色编码", "角色名称");

        // 内容
        List<List<Object>> datas = new ArrayList<>();

        for (SysRole role : roles) {
            // 加入每一行数据
            datas.add(CollUtil.list(true,
                    role.getRoleCode(),
                    role.getRoleName()));
        }


        BigExcelUtil.exportExcel(response, "角色信息", heads, datas);
    }
}
