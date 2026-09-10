package com.sense.app.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.sense.app.core.system.domain.CoreSysConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置 Mapper
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Mapper
public interface CoreSysConfigMapper extends MPJBaseMapper<CoreSysConfig> {
}
