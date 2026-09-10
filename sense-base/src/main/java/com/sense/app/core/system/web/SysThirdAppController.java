package com.sense.app.core.system.web;

import com.sense.app.core.system.domain.SysThirdApp;
import com.sense.app.core.system.service.SysThirdAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/core/third/app")
public class SysThirdAppController {

    @Autowired
    private SysThirdAppService service;

    @GetMapping("/query")
    public ResponseEntity<List<SysThirdApp>> query() {
        return new ResponseEntity<>(service.queryList(),HttpStatus.OK);
    }

    @GetMapping("/queryById")
    public ResponseEntity<SysThirdApp> queryById(@RequestParam(value = "id") Long id) {
        return new ResponseEntity<>(service.queryById(id),HttpStatus.OK);
    }
}
