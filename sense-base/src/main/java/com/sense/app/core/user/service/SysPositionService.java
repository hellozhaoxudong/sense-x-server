package com.sense.app.core.user.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.user.domain.SysPosition;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.domain.SysUserPosition;
import com.sense.app.core.user.mapper.SysPositionMapper;
import com.sense.app.core.user.mapper.SysUserMapper;
import com.sense.app.core.user.mapper.SysUserPositionMapper;
import com.sense.app.core.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysPositionService
 * @description 岗位管理Service
 * @author sense-x
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysPositionService extends ServiceImpl<SysPositionMapper, SysPosition> {

    @Autowired
    private SysUserPositionMapper userPositionMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserService userService;

    /**
     * 查询岗位列表
     */
    public List<SysPosition> queryPositions(Page<SysPosition> mybatisPage, String postName, String postCode) {
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
        if (postName != null && !postName.isEmpty()) {
            wrapper.like(SysPosition::getPostName, postName);
        }
        if (postCode != null && !postCode.isEmpty()) {
            wrapper.like(SysPosition::getPostCode, postCode);
        }
        wrapper.orderByAsc(SysPosition::getPostCode);
        Page<SysPosition> results = page(mybatisPage, wrapper);
        return results.getRecords();
    }

    /**
     * 提交岗位信息（新增/修改）
     */
    public void submitPosition(SysPosition data) {
        if (data.getTenantId() == null) {
            data.setTenantId(CurrentUser.getTenantId());
        }
        saveOrUpdate(data);
    }

    /**
     * 删除岗位信息
     */
    public void deletePositions(List<Long> ids) {
        // 删除岗位-用户分配信息
        userPositionMapper.delete(new QueryWrapper<SysUserPosition>().in("position_id", ids));
        // 删除岗位信息
        removeByIds(ids);
    }

    /**
     * 查询岗位下的用户
     */
    public List<SysUser> queryPositionUsers(Long positionId) {
        List<SysUserPosition> userPositions = userPositionMapper.selectList(
                new LambdaQueryWrapper<SysUserPosition>().eq(SysUserPosition::getPositionId, positionId));

        if (CollUtil.isEmpty(userPositions)) {
            return new ArrayList<>();
        }

        List<Long> userIds = userPositions.stream().map(SysUserPosition::getUserId).collect(Collectors.toList());
        return userMapper.selectList(new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getId, userIds)
                .select(SysUser::getId, SysUser::getUsername, SysUser::getName, SysUser::getSex, SysUser::getPhone, SysUser::getEmail));
    }

    /**
     * 查询岗位未添加的用户
     */
    public List<SysUser> queryUnPositionUsers(Long positionId) {
        List<SysUser> users = userService.querySimpleSysUsersInTenant();

        List<SysUserPosition> userPositions = userPositionMapper.selectList(
                new LambdaQueryWrapper<SysUserPosition>().eq(SysUserPosition::getPositionId, positionId));

        if (CollUtil.isEmpty(userPositions)) {
            return users;
        }

        List<Long> existUserIds = userPositions.stream().map(SysUserPosition::getUserId).collect(Collectors.toList());
        return users.stream().filter(u -> !existUserIds.contains(u.getId())).collect(Collectors.toList());
    }

    /**
     * 向岗位添加用户
     */
    public void addPositionUsers(Long positionId, List<Long> userIds) {
        List<SysUserPosition> datas = new ArrayList<>();
        for (Long userId : userIds) {
            datas.add(SysUserPosition.builder()
                    .positionId(positionId)
                    .userId(userId)
                    .tenantId(CurrentUser.getTenantId())
                    .build());
        }
        for (SysUserPosition data : datas) {
            userPositionMapper.insert(data);
        }
    }

    /**
     * 从岗位移除用户
     */
    public void removePositionUsers(Long positionId, List<Long> userIds) {
        userPositionMapper.delete(new LambdaQueryWrapper<SysUserPosition>()
                .eq(SysUserPosition::getPositionId, positionId)
                .in(SysUserPosition::getUserId, userIds));
    }
}
