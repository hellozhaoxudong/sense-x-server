package com.sense.app.base.rule.web;

import com.sense.app.base.rule.domain.BaseRuleFolder;
import com.sense.app.base.rule.service.RuleFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base/rule/folder")
public class RuleFolderWeb {

    @Autowired
    private RuleFolderService service;

    /**
     * 查询分类列表（不分页）
     * @param folderName 分类名称
     */
    @GetMapping("/list")
    public ResponseEntity<List<BaseRuleFolder>> queryFolderList(@RequestParam(value = "folderName", required = false) String folderName) {
        BaseRuleFolder query = new BaseRuleFolder();
        query.setFolderName(folderName);
        List<BaseRuleFolder> list = service.queryFolderList(query);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 提交分类（新增/修改）
     * @param data 分类信息
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitFolder(@RequestBody BaseRuleFolder data) {
        service.saveOrUpdate(data);
        return new ResponseEntity<>(data.getId(), HttpStatus.OK);
    }

    /**
     * 删除分类
     * @param ids 分类ID列表
     */
    @PostMapping("/delete")
    public ResponseEntity deleteFolder(@RequestBody List<Long> ids) {
        service.removeByIds(ids);
        return ResponseEntity.ok(true);
    }
}
