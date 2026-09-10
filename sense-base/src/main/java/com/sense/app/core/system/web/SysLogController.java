package com.sense.app.core.system.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.core.system.domain.SysLog;
import com.sense.app.core.system.service.SysLogService;
import com.sense.app.core.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shenfulong
 * @date 2026-01-01 23:00:00
 * @description 用户日志controller
 */
@RestController
@RequestMapping("/api/core/log")
public class SysLogController {

    @Autowired
    private SysLogService service;

    /**
     *
     * description 分页查询用户日志控制器
     * @author shenfulong
     * @date 2026-01-01 23:00:00
     */
    @PostMapping("/query")
    public ResponseEntity<List<SysLog>> querySysLog(@RequestParam(value = "page") int page,
                                                    @RequestParam(value = "pageSize") int pageSize,
                                                    @RequestBody SysLog log) {
        Page mybatisPage = PageUtil.getPage(page,pageSize);
        List<SysLog> sysLogs = service.queryRoles(mybatisPage,log);
        return new ResponseEntity<>(sysLogs, PageUtil.getTotalHeader(mybatisPage),HttpStatus.OK);
    }
}
