package com.sense.app.base.company.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.base.company.domain.BaseCompany;
import com.sense.app.base.company.service.CompanyService;
import com.sense.app.core.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base/company")
public class CompanyWeb {

    @Autowired
    private CompanyService service;

    /**
     * 分页查询企业列表
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param companyName 企业名称
     * @param companyCode 企业编码
     * @param socialCreditCode 统一社会信用代码
     * @param status 状态
     * @param page 当前页
     * @param pageSize 每页条数
     */
    @GetMapping("/page")
    public ResponseEntity<List<BaseCompany>> queryCompanyList(@RequestParam(value = "companyName", required = false) String companyName,
                                                           @RequestParam(value = "companyCode", required = false) String companyCode,
                                                           @RequestParam(value = "socialCreditCode", required = false) String socialCreditCode,
                                                           @RequestParam(value = "status", required = false) Integer status,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);
        
        BaseCompany query = new BaseCompany();
        query.setCompanyName(companyName);
        query.setCompanyCode(companyCode);
        query.setSocialCreditCode(socialCreditCode);
        query.setStatus(status);
        
        List<BaseCompany> companies = service.queryCompanyList(mybatisPage, query);
        return new ResponseEntity(companies, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 提交企业
     * @param data 企业信息
     * @return 企业ID
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitCompany(@RequestBody BaseCompany data) {
        service.saveOrUpdate(data);
        return new ResponseEntity<>(data.getId(), HttpStatus.OK);
    }

    /**
     * 删除企业
     * @param ids 企业ID列表
     * @return 删除结果
     */
    @PostMapping("/delete")
    public ResponseEntity deleteCompany(@RequestBody List<Long> ids) {
        service.removeByIds(ids);
        return ResponseEntity.ok(true);
    }
}
