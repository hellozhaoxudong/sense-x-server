package com.sense.app.core.agent.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.agent.domain.CoreAiModel;
import com.sense.app.core.agent.mapper.CoreAiModelMapper;
import com.sense.app.core.exception.BizException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 模型管理
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreAiModelService extends ServiceImpl<CoreAiModelMapper, CoreAiModel> {

    /**
     * 按类型查询模型列表
     *
     * @param modelType 模型类型：Chat、Embedding
     * @return 模型列表
     */
    public List<CoreAiModel> queryByType(String modelType) {
        LambdaQueryWrapper<CoreAiModel> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(modelType)) {
            wrapper.eq(CoreAiModel::getModelType, modelType);
        }
        return list(wrapper);
    }

    public List<CoreAiModel> queryChatModel(){
        return list(new LambdaQueryWrapper<CoreAiModel>().eq(CoreAiModel::getModelType, "Chat").eq(CoreAiModel::getEnable, "Y"));
    }

    /**
     * 新建/更新模型
     *
     * @param model 模型信息
     * @return 模型ID
     */
    public Long submit(CoreAiModel model) {
        if (StrUtil.isBlank(model.getModelTitle())) {
            throw new BizException("模型标题不能为空");
        }
        if (StrUtil.isBlank(model.getModelName())) {
            throw new BizException("模型名称不能为空");
        }
        if (StrUtil.isBlank(model.getModelType())) {
            throw new BizException("模型类型不能为空");
        }

        if (null == model.getId()) {
            save(model);
        } else {
            updateById(model);
        }
        return model.getId();
    }

    /**
     * 批量删除模型
     *
     * @param ids 模型ID集合
     */
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        removeByIds(ids);
    }
}

