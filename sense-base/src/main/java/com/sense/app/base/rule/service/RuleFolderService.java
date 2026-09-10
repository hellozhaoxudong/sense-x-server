package com.sense.app.base.rule.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.rule.domain.BaseRuleFolder;
import com.sense.app.base.rule.mapper.RuleFolderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RuleFolderService extends ServiceImpl<RuleFolderMapper, BaseRuleFolder> {

    /**
     * 查询分类列表（不分页）
     * @param folder 查询条件
     * @return 分类列表
     */
    public List<BaseRuleFolder> queryFolderList(BaseRuleFolder folder) {
        LambdaQueryWrapper<BaseRuleFolder> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.select()
                .like(StrUtil.isNotBlank(folder.getFolderName()), BaseRuleFolder::getFolderName, folder.getFolderName())
                .orderByDesc(BaseRuleFolder::getCreateDate);

        return list(queryWrapper);
    }
}
