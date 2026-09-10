package com.sense.app.core.ai.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.core.ai.domain.CoreAiSkill;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI技能
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface CoreAiSkillMapper extends BaseMapper<CoreAiSkill> {
}
