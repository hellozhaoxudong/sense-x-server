package com.sense.app.core.user.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.core.user.domain.SysTenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @ClassName SysTenantMapper
 * @description 租户管理Mapper（忽略租户）
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface SysTenantMapper extends BaseMapper<SysTenant> {

    /**
     * queryUserTenant : 查询某用户的入驻租户列表
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param userId 用户ID
     */
    List<SysTenant> queryUserTenant(@Param("userId") Long userId);
}
