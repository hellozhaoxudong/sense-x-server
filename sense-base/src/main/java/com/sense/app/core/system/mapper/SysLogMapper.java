package com.sense.app.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.sense.app.core.system.domain.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shenfulong
 * @date 2026-01-01 23:00:00
 * @description 用户日志Mapper
 */
@Mapper
public interface SysLogMapper extends MPJBaseMapper<SysLog> {

}
