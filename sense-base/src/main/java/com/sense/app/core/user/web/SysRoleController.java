package com.sense.app.core.user.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.core.user.domain.SysRole;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.service.SysRoleService;
import com.sense.app.core.utils.PageUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysRoleController
 * @description 角色管理接口
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/core/role")
public class SysRoleController {

    @Autowired
    private SysRoleService service;

    /**
     * queryRoles : 查询角色信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param page      当前页
     * @param pageSize  每页条数
     */
    @GetMapping("/query")
    public ResponseEntity<List<SysRole>> queryRoles(@RequestParam(value = "page", defaultValue = "1") int page,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){

        Page mybatisPage = PageUtil.getPage(page, pageSize);
        return new ResponseEntity(service.queryRoles(mybatisPage), PageUtil.getTotalHeader(mybatisPage),HttpStatus.OK);
    }

    /**
     * submitRole : 提交角色信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param data 角色信息
     */
    @PostMapping("/submit")
    public ResponseEntity submitRole(@RequestBody SysRole data){

        service.submitRole(data);
        return ResponseEntity.ok(true);
    }

    /**
     * deleteRoles : 删除角色信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids 角色ID集合
     */
    @PostMapping("/delete")
    public ResponseEntity deleteRoles(@RequestBody List<Long> ids){
        service.deleteRoles(ids);
        return ResponseEntity.ok(true);
    }

    /**
     * 查询角色成员信息
     * @param roleId 角色ID
     */
    @GetMapping("/member/query")
    public ResponseEntity<List<SysUser>> queryMembers(@RequestParam(value = "roleId") Long roleId) {
        return ResponseEntity.ok(service.queryMembers(roleId));
    }

    /**
     * 查询角色未添加的用户
     * @param roleId 角色ID
     */
    @GetMapping("/unmember/query")
    public ResponseEntity<List<SysUser>> queryUnMembers(@RequestParam(value = "roleId") Long roleId) {
        return ResponseEntity.ok(service.queryUnMembers(roleId));
    }


    /**
     * 批量添加角色成员
     * @param roleId 角色ID
     * @param userIds 成员ID集合
     */
    @PostMapping("/member/add")
    public ResponseEntity addMembers(@RequestParam(value = "roleId") Long roleId,
                                     @RequestBody List<Long> userIds) {
        service.addMembers(roleId, userIds);
        return ResponseEntity.ok(true);
    }

    /**
     * 批量删除角色成员
     * @param roleId 角色ID
     * @param userIds 成员ID集合
     */
    @PostMapping("/member/remove")
    public ResponseEntity deleteMembers(@RequestParam(value = "roleId") Long roleId,
                                        @RequestBody List<Long> userIds) {
        service.removeMembers(roleId, userIds);
        return ResponseEntity.ok(true);
    }

    /**
     * 导出角色信息
     */
    @GetMapping("/export")
    public void exportRoles(HttpServletResponse response){
        service.exportRoles(response);
    }
}