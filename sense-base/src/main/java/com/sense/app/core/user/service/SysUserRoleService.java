package com.sense.app.core.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.user.domain.SysUserRole;
import com.sense.app.core.user.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName SysUserRoleService
 * @description 用户角色分配Service
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> {

}
