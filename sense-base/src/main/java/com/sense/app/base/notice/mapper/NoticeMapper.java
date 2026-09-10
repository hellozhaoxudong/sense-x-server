package com.sense.app.base.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.base.notice.domain.BaseNotice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper extends BaseMapper<BaseNotice> {
}
