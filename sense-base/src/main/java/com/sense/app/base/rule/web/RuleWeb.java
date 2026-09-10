package com.sense.app.base.rule.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.base.rule.domain.BaseRule;
import com.sense.app.base.rule.domain.BaseRuleRun;
import com.sense.app.base.rule.service.RuleRunService;
import com.sense.app.base.rule.service.RuleService;
import com.sense.app.core.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base/rule")
public class RuleWeb {

    @Autowired
    private RuleService service;

    @Autowired
    private RuleRunService ruleRunService;

    /**
     * 分页查询规则列表
     * @param folderId 分类ID
     * @param ruleName 规则名称
     * @param ruleCode 规则编码
     * @param ruleStatus 规则状态
     * @param page 当前页
     * @param pageSize 每页条数
     */
    @GetMapping("/page")
    public ResponseEntity<List<BaseRule>> queryRuleList(@RequestParam(value = "folderId", required = false) Long folderId,
                                                        @RequestParam(value = "ruleName", required = false) String ruleName,
                                                        @RequestParam(value = "ruleCode", required = false) String ruleCode,
                                                        @RequestParam(value = "ruleStatus", required = false) String ruleStatus,
                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);

        BaseRule query = new BaseRule();
        query.setFolderId(folderId);
        query.setRuleName(ruleName);
        query.setRuleCode(ruleCode);
        query.setRuleStatus(ruleStatus);

        List<BaseRule> rules = service.queryRuleList(mybatisPage, query);
        return new ResponseEntity(rules, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 提交规则（新增/修改）
     * @param data 规则信息
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitRule(@RequestBody BaseRule data) {
        service.saveOrUpdate(data);
        return new ResponseEntity<>(data.getId(), HttpStatus.OK);
    }

    /**
     * 删除规则
     * @param ids 规则ID列表
     */
    @PostMapping("/delete")
    public ResponseEntity deleteRule(@RequestBody List<Long> ids) {
        service.removeByIds(ids);
        return ResponseEntity.ok(true);
    }

    /**
     * 规则上线
     * @param id 规则ID
     */
    @PostMapping("/online/{id}")
    public ResponseEntity onlineRule(@PathVariable Long id) {
        service.onlineRule(id);
        return ResponseEntity.ok(true);
    }

    /**
     * 运行规则
     * @param id 规则ID
     */
    @PostMapping("/run/{id}")
    public ResponseEntity<BaseRuleRun> runRule(@PathVariable Long id) {
        BaseRuleRun runLog = ruleRunService.runRule(id);
        return new ResponseEntity<>(runLog, HttpStatus.OK);
    }

    /**
     * 分页查询规则运行日志
     * @param ruleId 规则ID
     * @param page 当前页
     * @param pageSize 每页条数
     */
    @GetMapping("/runLog")
    public ResponseEntity<List<BaseRuleRun>> queryRunLogs(@RequestParam(value = "ruleId") String ruleId,
                                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);
        List<BaseRuleRun> logs = ruleRunService.queryRunLogs(mybatisPage, ruleId);
        return new ResponseEntity(logs, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 分页查询运行日志页面（关联规则+分类，支持按规则编码/名称搜索）
     * @param ruleCode 规则编码
     * @param ruleName 规则名称
     * @param page 当前页
     * @param pageSize 每页条数
     */
    @GetMapping("/runLog/page")
    public ResponseEntity<List<BaseRuleRun>> queryRunLogPage(@RequestParam(value = "ruleCode", required = false) String ruleCode,
                                                             @RequestParam(value = "ruleName", required = false) String ruleName,
                                                             @RequestParam(value = "page", defaultValue = "1") int page,
                                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<BaseRuleRun> mybatisPage = PageUtil.getPage(page, pageSize);
        List<BaseRuleRun> logs = ruleRunService.queryRunLogPage(mybatisPage, ruleCode, ruleName);
        return new ResponseEntity(logs, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }
}
