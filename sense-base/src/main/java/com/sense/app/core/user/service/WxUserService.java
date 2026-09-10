package com.sense.app.core.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.exception.NoAuthException;
import com.sense.app.core.user.domain.SysTenant;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.domain.WxUser;
import com.sense.app.core.user.mapper.SysTenantMapper;
import com.sense.app.core.user.mapper.WxUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 微信注册用户
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WxUserService extends ServiceImpl<WxUserMapper, WxUser> {

    @Autowired
    private SysTenantMapper tenantMapper;

    /**
     * 查询用户信息
     */
    public WxUser queryWxUser(String loginName){
        WxUser wxUser = null;

        // 尝试使用微信ID、手机号查询
        wxUser = baseMapper.selectOne(new LambdaQueryWrapper<WxUser>().eq(WxUser::getWxId, loginName).or().eq(WxUser::getPhone, loginName));

        return wxUser;
    }

    /**
     * 自动注册用户
     */
    public WxUser autoRegister(String wxId, String phone){
        WxUser wxUser = WxUser.builder()
                .id(IdWorker.getId())
                .wxId(wxId)
                .phone(phone)
                .locked("N")
                .enabled("Y").build();

        baseMapper.insert(wxUser);

        return wxUser;
    }

    /**
     * 获取全量用户信息
     */
    public SysUser buildFullUserInfo(WxUser wxUser, String tenantCode){
        // 补充租户信息
        SysTenant tenant = tenantMapper.selectOne(new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getTenantCode, tenantCode));
        if (tenant == null) {
            throw new NoAuthException("租户不存在，请联系管理员");
        }

        SysUser sysUser = SysUser.builder()
                .id(wxUser.getId())
                .username(wxUser.getPhone())
                .name(wxUser.getName())
                .phone(wxUser.getPhone())
                .locked(wxUser.getLocked())
                .enabled(wxUser.getEnabled())
                .tenant(tenant)
                .build();

        return sysUser;
    }
}
