package com.sense.app.core.user.web;

import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.service.SysUserOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysUserController
 * @description 用户管理接口
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/core/user/organize")
public class SysUserOrganizeController {

    @Autowired
    private SysUserOrganizeService service;

    /**
     * 查询组织下的用户
     */
    @GetMapping("/query")
    public ResponseEntity<List<SysUser>> querySysUsers(@RequestParam(value = "organizeId") Long organizeId,
                                                       @RequestParam(value = "loginname", required = false) String loginname,
                                                       @RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "phone", required = false) String phone,
                                                       @RequestParam(value = "email", required = false) String email){


        List<SysUser> sysUsers = service.queryUser(organizeId, loginname, name, phone, email);
        return new ResponseEntity(sysUsers,HttpStatus.OK);
    }

    /**
     * 向组织添加用户
     */
    @PostMapping("/submit")
    public ResponseEntity submitSysUsers(@RequestParam(value = "organizeId") Long organizeId,
                                                       @RequestBody List<Long> userIds){

        service.submitUser(organizeId, userIds);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 向组织添加用户
     */
    @PostMapping("/delete")
    public ResponseEntity deleteSysUsers(@RequestParam(value = "organizeId") Long organizeId,
                                                       @RequestBody List<Long> userIds){

        service.deleteUser(organizeId, userIds);
        return new ResponseEntity(HttpStatus.OK);
    }
}