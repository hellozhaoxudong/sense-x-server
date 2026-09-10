package com.sense.app.base.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.base.file.domain.BaseFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseFileMapper extends BaseMapper<BaseFile> {
}
