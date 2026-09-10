package com.sense.app.base.rule.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.rule.domain.BaseRule;
import com.sense.app.base.rule.mapper.RuleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RuleService extends ServiceImpl<RuleMapper, BaseRule> {

    /**
     * 分页查询规则列表
     * @param mybatisPage 分页参数
     * @param rule 查询条件
     * @return 规则列表
     */
    public List<BaseRule> queryRuleList(Page mybatisPage, BaseRule rule) {
        LambdaQueryWrapper<BaseRule> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.select()
                .eq(rule.getFolderId() != null, BaseRule::getFolderId, rule.getFolderId())
                .like(StrUtil.isNotBlank(rule.getRuleName()), BaseRule::getRuleName, rule.getRuleName())
                .like(StrUtil.isNotBlank(rule.getRuleCode()), BaseRule::getRuleCode, rule.getRuleCode())
                .eq(StrUtil.isNotBlank(rule.getRuleStatus()), BaseRule::getRuleStatus, rule.getRuleStatus())
                .orderByDesc(BaseRule::getCreateDate);

        Page<BaseRule> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }

    /**
     * 规则上线：将editContent复制到onlineContent，状态改为已上线
     * @param id 规则ID
     */
    public void onlineRule(Long id) {
        BaseRule rule = getById(id);
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }
        rule.setOnlineContent(rule.getEditContent());
        rule.setRuleStatus("已上线");
        updateById(rule);
    }
}
