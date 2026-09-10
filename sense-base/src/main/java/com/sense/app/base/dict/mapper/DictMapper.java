package com.sense.app.base.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.base.dict.domain.BaseDict;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictMapper extends BaseMapper<BaseDict> {
}
