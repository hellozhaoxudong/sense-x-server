package com.sense.app.base.dict.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.dict.domain.BaseDict;
import com.sense.app.base.dict.mapper.DictMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DictService extends ServiceImpl<DictMapper, BaseDict> {

    /**
     * 分页查询字典列表
     * @param mybatisPage 分页参数
     * @param dict 查询条件
     * @return 字典列表
     */
    public List<BaseDict> queryDictList(Page mybatisPage, BaseDict dict) {
        LambdaQueryWrapper<BaseDict> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.select()
                .like(StrUtil.isNotBlank(dict.getDictName()), BaseDict::getDictName, dict.getDictName())
                .like(StrUtil.isNotBlank(dict.getDictCode()), BaseDict::getDictCode, dict.getDictCode())
                .orderByDesc(BaseDict::getCreateDate);

        Page<BaseDict> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }
}
