package com.sense.app.core.ai.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.ai.domain.CoreAiSkill;
import com.sense.app.core.ai.mapper.CoreAiSkillMapper;
import com.sense.app.core.exception.BizException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AI技能管理
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreAiSkillService extends ServiceImpl<CoreAiSkillMapper, CoreAiSkill> {

    /**
     * 查询技能列表
     */
    public List<CoreAiSkill> queryList(String skillName, String skillTag) {
        LambdaQueryWrapper<CoreAiSkill> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(skillName)) {
            wrapper.like(CoreAiSkill::getSkillName, skillName);
        }
        if (StrUtil.isNotBlank(skillTag)) {
            wrapper.eq(CoreAiSkill::getSkillTag, skillTag);
        }
        wrapper.orderByDesc(CoreAiSkill::getCreateDate);
        return list(wrapper);
    }

    /**
     * 新建/更新技能
     */
    public Long submit(CoreAiSkill skill) {
        if (StrUtil.isBlank(skill.getSkillName())) {
            throw new BizException("技能名称不能为空");
        }
        if (null == skill.getId()) {
            save(skill);
        } else {
            updateById(skill);
        }
        return skill.getId();
    }

    /**
     * 批量删除技能
     */
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        removeByIds(ids);
    }
}
