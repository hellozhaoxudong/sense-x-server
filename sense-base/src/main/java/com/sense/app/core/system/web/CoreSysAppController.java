package com.sense.app.core.system.web;

import com.sense.app.core.system.domain.CoreSysApp;
import com.sense.app.core.system.service.CoreSysAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统应用管理控制器
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/core/sys/app")
public class CoreSysAppController {

    @Autowired
    private CoreSysAppService service;

    /**
     * 查询系统应用列表（不分页）
     * @param appCode 应用编码（可选，模糊查询）
     * @param appName 应用名称（可选，模糊查询）
     * @return 应用列表
     */
    @GetMapping("/query")
    public ResponseEntity<List<CoreSysApp>> queryData(@RequestParam(value = "appCode", required = false) String appCode,
                                                      @RequestParam(value = "appName", required = false) String appName) {
        List<CoreSysApp> list = service.queryData(appCode, appName);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 提交系统应用（新增或更新）
     * @param data 应用信息
     * @return 应用ID
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitData(@RequestBody CoreSysApp data) {
        service.submitData(data);
        return new ResponseEntity<>(data.getId(), HttpStatus.OK);
    }

    /**
     * 删除系统应用
     * @param ids 应用ID列表
     * @return 删除结果
     */
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteData(@RequestBody List<Long> ids) {
        service.deleteData(ids);
        return ResponseEntity.ok(true);
    }

}
