package com.sense.app.core.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.sense.app.core.system.domain.CoreSysApp;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统应用 Mapper
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface CoreSysAppMapper extends MPJBaseMapper<CoreSysApp> {
}
