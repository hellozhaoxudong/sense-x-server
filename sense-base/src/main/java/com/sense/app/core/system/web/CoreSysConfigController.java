package com.sense.app.core.system.web;

import com.sense.app.core.system.domain.CoreSysConfig;
import com.sense.app.core.system.service.CoreSysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统配置控制器
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/core/sys/config")
public class CoreSysConfigController {

    @Autowired
    private CoreSysConfigService service;

    /**
     * 查询系统配置列表（不分页）
     * @param configKey 配置KEY（可选，模糊查询）
     * @return 配置列表
     */
    @GetMapping("/query")
    public ResponseEntity<List<CoreSysConfig>> queryList(@RequestParam(value = "configKey", required = false) String configKey) {
        List<CoreSysConfig> list = service.queryList(configKey);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/map/query")
    public ResponseEntity<Map<String, String>> queryConfigMap(@RequestParam(value = "configKey", required = false) String configKey) {
        return new ResponseEntity<>(service.queryConfigMap(configKey), HttpStatus.OK);
    }

    /**
     * 提交系统配置（根据configKey是否存在判断新增或更新）
     * @param data 配置信息
     * @return 配置ID
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submit(@RequestBody CoreSysConfig data) {
        Long id = service.submit(data);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
