package com.sense.app.core.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.sense.app.core.system.domain.CoreSysMenu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface CoreSysMenuMapper extends MPJBaseMapper<CoreSysMenu> {
}
