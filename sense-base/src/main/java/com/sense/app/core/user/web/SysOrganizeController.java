package com.sense.app.core.user.web;

import com.sense.app.core.user.domain.SysOrganize;
import com.sense.app.core.user.service.SysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core/organize")
public class SysOrganizeController {

    @Autowired
    private SysOrganizeService service;


    /**
     * 查询部门树
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    @GetMapping("/tree")
    public ResponseEntity<List<SysOrganize>> queryOrganizeTree(){
        return new ResponseEntity<>(service.queryOrganizeTree(), HttpStatus.OK);
    }

    /**
     * 提交部门
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitMenu(@RequestBody SysOrganize data) {
        return new ResponseEntity<>(service.submitOrganize(data), HttpStatus.OK);
    }

    /**
     * 删除部门
     */
    @PostMapping("/delete")
    public ResponseEntity deleteMenu(@RequestBody List<Long> ids) {
        service.deleteById(ids);
        return ResponseEntity.ok(true);
    }
}