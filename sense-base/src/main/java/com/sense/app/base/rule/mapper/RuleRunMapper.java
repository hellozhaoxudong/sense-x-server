package com.sense.app.base.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.base.rule.domain.BaseRuleRun;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RuleRunMapper extends BaseMapper<BaseRuleRun> {

    /**
     * 分页查询运行日志（关联规则表和分类表）
     */
    List<BaseRuleRun> queryRunLogPage(Page<BaseRuleRun> page,
                                      @Param("ruleCode") String ruleCode,
                                      @Param("ruleName") String ruleName);
}
