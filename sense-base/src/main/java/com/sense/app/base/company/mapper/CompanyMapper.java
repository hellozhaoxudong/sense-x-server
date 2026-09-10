package com.sense.app.base.company.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sense.app.base.company.domain.BaseCompany;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper extends BaseMapper<BaseCompany> {
}
