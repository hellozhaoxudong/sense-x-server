package com.sense.app.base.dict.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.dict.domain.BaseDictValue;
import com.sense.app.base.dict.mapper.DictValueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DictValueService extends ServiceImpl<DictValueMapper, BaseDictValue> {

    /**
     * 分页查询字典值列表
     * @param mybatisPage 分页参数
     * @param dictValue 查询条件
     * @return 字典值列表
     */
    public List<BaseDictValue> queryDictValueList(Page mybatisPage, BaseDictValue dictValue) {
        LambdaQueryWrapper<BaseDictValue> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.select()
                .eq(StrUtil.isNotBlank(dictValue.getDictCode()), BaseDictValue::getDictCode, dictValue.getDictCode())
                .like(StrUtil.isNotBlank(dictValue.getValueName()), BaseDictValue::getValueName, dictValue.getValueName())
                .eq(StrUtil.isNotBlank(dictValue.getStatus()), BaseDictValue::getStatus, dictValue.getStatus())
                .orderByAsc(BaseDictValue::getSort)
                .orderByDesc(BaseDictValue::getCreateDate);
        
        Page<BaseDictValue> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }
}
