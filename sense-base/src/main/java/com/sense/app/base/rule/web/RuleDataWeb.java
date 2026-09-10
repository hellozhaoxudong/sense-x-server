package com.sense.app.base.rule.web;

import com.sense.app.base.rule.domain.BaseRuleData;
import com.sense.app.base.rule.service.RuleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规则数据管理
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/base/ruleData")
public class RuleDataWeb {

    @Autowired
    private RuleDataService service;

    /**
     * 查询规则数据列表（不分页）
     */
    @GetMapping("/list")
    public ResponseEntity<List<BaseRuleData>> queryRuleDataList(@RequestParam(value = "dataName", required = false) String dataName,
                                                                 @RequestParam(value = "dataCode", required = false) String dataCode) {
        return new ResponseEntity<>(service.queryRuleDataList(dataName, dataCode), HttpStatus.OK);
    }

    /**
     * 提交规则数据（新增/修改）
     * @param data 规则数据信息
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitRuleData(@RequestBody BaseRuleData data) {
        service.saveOrUpdate(data);
        return new ResponseEntity<>(data.getId(), HttpStatus.OK);
    }

    /**
     * 删除规则数据（同时级联删除数据详情）
     * @param ids 规则数据ID列表
     */
    @PostMapping("/delete")
    public ResponseEntity deleteRuleData(@RequestBody List<Long> ids) {
        service.deleteData(ids);
        return ResponseEntity.ok(true);
    }
}
