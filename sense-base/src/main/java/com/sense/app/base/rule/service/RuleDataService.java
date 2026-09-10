package com.sense.app.base.rule.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.rule.domain.BaseRuleData;
import com.sense.app.base.rule.domain.BaseRuleDataDetail;
import com.sense.app.base.rule.mapper.RuleDataDetailMapper;
import com.sense.app.base.rule.mapper.RuleDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 规则数据服务
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RuleDataService extends ServiceImpl<RuleDataMapper, BaseRuleData> {

    @Autowired
    private RuleDataDetailMapper ruleDataDetailMapper;

    /**
     * 查询规则数据列表（不分页）
     */
    public List<BaseRuleData> queryRuleDataList(String dataName, String dataCode) {
        return list(new LambdaQueryWrapper<BaseRuleData>()
                .like(StrUtil.isNotBlank(dataName), BaseRuleData::getDataName, dataName)
                .like(StrUtil.isNotBlank(dataCode), BaseRuleData::getDataCode, dataCode)
                .orderByDesc(BaseRuleData::getCreateDate));
    }

    /**
     * 删除规则数据（同时级联删除其下所有数据详情）
     * @param ids 规则数据ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteData(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }

        // 删除规则数据详情
        ruleDataDetailMapper.delete(new LambdaQueryWrapper<BaseRuleDataDetail>().in(BaseRuleDataDetail::getRuleDataId, ids));

        // 删除规则数据
        removeByIds(ids);
    }
}
