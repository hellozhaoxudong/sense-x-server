package com.sense.app.core.user.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.domain.SysUserOrganize;
import com.sense.app.core.user.mapper.SysUserMapper;
import com.sense.app.core.user.mapper.SysUserOrganizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysUserOrganizeService
 * @description 用户组织分配Service
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserOrganizeService extends ServiceImpl<SysUserOrganizeMapper, SysUserOrganize> {

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询组织下的用户
     */
    public List<SysUser> queryUser(Long organizeId, String loginname, String name, String email, String phone ){
        // 查询组织下所有用户
        List<SysUserOrganize> organizeUsers = list(new LambdaQueryWrapper<SysUserOrganize>().eq(SysUserOrganize::getOrganizeId, organizeId));
        if (CollUtil.isEmpty(organizeUsers)){
            return new ArrayList<>();
        }

        // 查询用户
        List<Long> userIds = organizeUsers.stream().map(SysUserOrganize::getUserId).collect(Collectors.toList());
        List<SysUser> sysUsers = userMapper.selectList(new LambdaQueryWrapper<SysUser>().in(SysUser::getId, userIds)
                .like(StrUtil.isNotBlank(loginname), SysUser::getUsername, loginname)
                .like(StrUtil.isNotBlank(name), SysUser::getName, name)
                .like(StrUtil.isNotBlank(email), SysUser::getEmail, email)
                .like(StrUtil.isNotBlank(phone), SysUser::getPhone, phone));


        return sysUsers;
    }


    /**
     * 向组织添加用户
     */
    public void submitUser(Long organizeId, List<Long> userIds){
        List<SysUserOrganize> datas = new ArrayList<>();

        for (Long userId : userIds){
            datas.add(SysUserOrganize.builder().organizeId(organizeId).userId(userId).build());
        }

        saveBatch(datas);
    }

    /**
     * 从组织移除用户
     */
    public void deleteUser(Long organizeId, List<Long> userIds){
        remove(new LambdaQueryWrapper<SysUserOrganize>().eq(SysUserOrganize::getOrganizeId, organizeId).in(SysUserOrganize::getUserId, userIds));
    }
}
