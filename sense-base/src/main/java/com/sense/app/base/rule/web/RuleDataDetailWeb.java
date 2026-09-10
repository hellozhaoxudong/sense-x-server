package com.sense.app.base.rule.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.base.rule.domain.BaseRuleDataDetail;
import com.sense.app.base.rule.service.RuleDataDetailService;
import com.sense.app.core.utils.PageUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 规则数据详情管理
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/base/ruleDataDetail")
public class RuleDataDetailWeb {

    @Autowired
    private RuleDataDetailService service;

    /**
     * 分页查询规则数据详情
     * @param ruleDataId 规则数据ID
     * @param page 当前页
     * @param pageSize 每页条数
     */
    @GetMapping("/page")
    public ResponseEntity<List<BaseRuleDataDetail>> queryDetailPage(@RequestParam("ruleDataId") Long ruleDataId,
                                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);
        List<BaseRuleDataDetail> details = service.queryDetailPage(mybatisPage, ruleDataId);
        return new ResponseEntity(details, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 导入数据详情（文件内容需为JSON数组）
     * @param ruleDataId 规则数据ID
     * @param file 上传文件
     */
    @PostMapping("/import")
    public ResponseEntity importDetail(@RequestParam("ruleDataId") Long ruleDataId,
                                        @RequestParam("file") MultipartFile file) {
        service.importDetail(ruleDataId, file);
        return ResponseEntity.ok(true);
    }

    /**
     * 导出数据详情
     * @param ruleDataId 规则数据ID
     */
    @GetMapping("/export")
    public void exportDetail(@RequestParam("ruleDataId") Long ruleDataId, HttpServletResponse response) {
        service.exportDetail(response, ruleDataId);
    }

    /**
     * 清空规则数据下的所有详情
     * @param ruleDataId 规则数据ID
     */
    @PostMapping("/clear")
    public ResponseEntity clearDetail(@RequestParam("ruleDataId") Long ruleDataId) {
        service.clearDetail(ruleDataId);
        return ResponseEntity.ok(true);
    }
}
