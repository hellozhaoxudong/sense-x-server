package com.sense.app.core.user.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.service.SysUserService;
import com.sense.app.core.utils.PageUtil;
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
@RequestMapping("/api/core/user")
public class SysUserController {

    @Autowired
    private SysUserService service;

    /**
     * 查询当前租户下用户简易信息
     * @param filter        筛选条件
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    @GetMapping("/simple")
    public ResponseEntity<List<SysUser>> querySimpleSysUsers(@RequestParam(value = "filter", required = false) String filter){
        List<SysUser> sysUsers = service.querySimpleSysUsers(filter);
        return new ResponseEntity(sysUsers, HttpStatus.OK);
    }

    /**
     * querySysUsers : 查询当前租户下的所有用户
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param username	登录名
     * @param name	    用户名
     * @param phone	    电话
     * @param enabled	启用状态
     * @param page      当前页
     * @param pageSize  每页条数
     */

    @GetMapping("/query")
    public ResponseEntity<List<SysUser>> querySysUsers(@RequestParam(value = "username", required = false) String username,
                                                       @RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "phone", required = false) String phone,
                                                       @RequestParam(value = "enabled", required = false) Integer enabled,
                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page mybatisPage = PageUtil.getPage(page, pageSize);

        List<SysUser> sysUsers = service.queryUsers(mybatisPage, username, name, phone, enabled);
        return new ResponseEntity(sysUsers, PageUtil.getTotalHeader(mybatisPage),HttpStatus.OK);
    }

    /**
     * submitSysUser : 提交用户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param sysUser	用户信息
     */
    @PostMapping("/submit")
    public ResponseEntity submitSysUser(@RequestBody SysUser sysUser){

        service.submitSysUser(sysUser);
        return ResponseEntity.ok(true);
    }

    /**
     * 修改我本人的基础信息
     */
    @PostMapping("/my/info/change")
    public ResponseEntity changeMyInfo(@RequestBody SysUser sysUser){
        service.changeMyInfo(sysUser);
        return ResponseEntity.ok(true);
    }

    /**
     * 修改我本人的密码
     */
    @GetMapping("/my/password/change")
    public ResponseEntity changeMyPassword(@RequestParam(value = "oldPassword") String oldPassword,
                                           @RequestParam(value = "newPassword") String newPassword){
        service.changeMyPassword(oldPassword, newPassword);
        return ResponseEntity.ok(true);
    }

    /**
     * deleteSysUser : 删除用户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids 用户IDS
     */
    @PostMapping("/delete")
    public ResponseEntity deleteSysUser(@RequestBody List<Long> ids){
        service.deleteSysUser(ids);
        return ResponseEntity.ok(true);
    }

    /**
     * enableSysUser : 启用用户
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids 用户id集合
     */
    @PostMapping("/enabled")
    public ResponseEntity enableSysUser(@RequestBody List<Long> ids){
        service.enableSysUser(ids);
        return ResponseEntity.ok(true);
    }

    /**
     * disableSysUser : 禁用用户
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids 用户id集合
     */
    @PostMapping("/disabled")
    public ResponseEntity disableSysUser(@RequestBody List<Long> ids){
        service.disableSysUser(ids);
        return ResponseEntity.ok(true);
    }

    /**
     * distributeRoles : 给用户分配角色
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param userId	用户ID
     * @param roleIds	角色ID集合
     */
    @PostMapping("/role/distribute")
    public ResponseEntity distributeRoles(@RequestParam(value = "userId") Long userId, @RequestBody List<Long> roleIds){
        service.distributeRoles(userId, roleIds);
        return ResponseEntity.ok(true);
    }
}