package com.sense.app.base.company.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.base.company.domain.BaseCompany;
import com.sense.app.base.company.mapper.CompanyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyService extends ServiceImpl<CompanyMapper, BaseCompany> {

    /**
     * 分页查询企业列表
     * @param mybatisPage 分页参数
     * @param company 查询条件
     * @return 企业列表
     */
    public List<BaseCompany> queryCompanyList(Page mybatisPage, BaseCompany company) {
        LambdaQueryWrapper<BaseCompany> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.select()
                .like(StrUtil.isNotBlank(company.getCompanyName()), BaseCompany::getCompanyName, company.getCompanyName())
                .like(StrUtil.isNotBlank(company.getCompanyCode()), BaseCompany::getCompanyCode, company.getCompanyCode())
                .like(StrUtil.isNotBlank(company.getSocialCreditCode()), BaseCompany::getSocialCreditCode, company.getSocialCreditCode())
                .eq(company.getStatus() != null, BaseCompany::getStatus, company.getStatus())
                .orderByDesc(BaseCompany::getCreateDate);
        
        Page<BaseCompany> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }
}
