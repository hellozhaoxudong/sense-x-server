package com.sense.app.base.rule.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.rule.domain.BaseRule;
import com.sense.app.base.rule.domain.BaseRuleRun;
import com.sense.app.base.rule.mapper.RuleRunMapper;
import com.sense.app.base.rule.run.AviatorRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RuleRunService extends ServiceImpl<RuleRunMapper, BaseRuleRun> {

    @Autowired
    private RuleService ruleService;

    /**
     * 运行规则：执行onlineContent，记录日志
     * @param ruleId 规则ID
     * @return 运行日志记录
     */
    public BaseRuleRun runRule(Long ruleId) {
        BaseRule rule = ruleService.getById(ruleId);
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }
        if (!"已上线".equals(rule.getRuleStatus())) {
            throw new RuntimeException("规则未上线，无法运行");
        }

        String script = rule.getOnlineContent();
        Date startDate = new Date();

        // 执行脚本，参数传空
        String result = AviatorRunner.runScriptReString(
                "rule_" + rule.getId(),
                script,
                new HashMap<>()
        );

        Date endDate = new Date();

        // 保存运行日志
        BaseRuleRun runLog = BaseRuleRun.builder()
                .ruleId(String.valueOf(ruleId))
                .ruleContent(script)
                .ruleParams("{}")
                .startDate(startDate)
                .endDate(endDate)
                .runResult(result)
                .build();
        save(runLog);

        return runLog;
    }

    /**
     * 分页查询规则运行日志
     * @param mybatisPage 分页参数
     * @param ruleId 规则ID
     * @return 日志列表
     */
    public List<BaseRuleRun> queryRunLogs(Page mybatisPage, String ruleId) {
        LambdaQueryWrapper<BaseRuleRun> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseRuleRun::getRuleId, ruleId)
                .orderByDesc(BaseRuleRun::getStartDate);

        Page<BaseRuleRun> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }

    /**
     * 分页查询运行日志页面（关联规则+分类）
     * @param mybatisPage 分页参数
     * @param ruleCode 规则编码
     * @param ruleName 规则名称
     * @return 日志列表
     */
    public List<BaseRuleRun> queryRunLogPage(Page<BaseRuleRun> mybatisPage, String ruleCode, String ruleName) {
        return baseMapper.queryRunLogPage(mybatisPage, ruleCode, ruleName);
    }
}
