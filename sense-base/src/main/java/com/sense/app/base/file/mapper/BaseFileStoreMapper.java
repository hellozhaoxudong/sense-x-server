package com.sense.app.base.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.base.file.domain.BaseFileStore;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseFileStoreMapper extends BaseMapper<BaseFileStore> {
}
