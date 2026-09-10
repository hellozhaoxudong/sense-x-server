package com.sense.app.base.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.base.rule.domain.BaseRule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RuleMapper extends BaseMapper<BaseRule> {
}
