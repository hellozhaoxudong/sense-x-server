package com.sense.app.core.user.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.core.user.domain.SysPosition;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.service.SysPositionService;
import com.sense.app.core.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysPositionController
 * @description 岗位管理接口
 * @author sense-x
 */
@RestController
@RequestMapping("/api/core/position")
public class SysPositionController {

    @Autowired
    private SysPositionService service;

    /**
     * 查询岗位列表
     */
    @GetMapping("/query")
    public ResponseEntity<List<SysPosition>> queryPositions(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
            @RequestParam(value = "postName", required = false) String postName,
            @RequestParam(value = "postCode", required = false) String postCode) {

        Page<SysPosition> mybatisPage = PageUtil.getPage(page, pageSize);
        return new ResponseEntity(service.queryPositions(mybatisPage, postName, postCode), PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 提交岗位信息（新增/修改）
     */
    @PostMapping("/submit")
    public ResponseEntity submitPosition(@RequestBody SysPosition data) {
        service.submitPosition(data);
        return ResponseEntity.ok(true);
    }

    /**
     * 删除岗位信息
     */
    @PostMapping("/delete")
    public ResponseEntity deletePositions(@RequestBody List<Long> ids) {
        service.deletePositions(ids);
        return ResponseEntity.ok(true);
    }

    /**
     * 查询岗位下的用户
     */
    @GetMapping("/user/query")
    public ResponseEntity<List<SysUser>> queryPositionUsers(@RequestParam(value = "positionId") Long positionId) {
        return ResponseEntity.ok(service.queryPositionUsers(positionId));
    }

    /**
     * 查询岗位未添加的用户
     */
    @GetMapping("/user/unquery")
    public ResponseEntity<List<SysUser>> queryUnPositionUsers(@RequestParam(value = "positionId") Long positionId) {
        return ResponseEntity.ok(service.queryUnPositionUsers(positionId));
    }

    /**
     * 向岗位添加用户
     */
    @PostMapping("/user/add")
    public ResponseEntity addPositionUsers(@RequestParam(value = "positionId") Long positionId,
                                           @RequestBody List<Long> userIds) {
        service.addPositionUsers(positionId, userIds);
        return ResponseEntity.ok(true);
    }

    /**
     * 从岗位移除用户
     */
    @PostMapping("/user/remove")
    public ResponseEntity removePositionUsers(@RequestParam(value = "positionId") Long positionId,
                                              @RequestBody List<Long> userIds) {
        service.removePositionUsers(positionId, userIds);
        return ResponseEntity.ok(true);
    }
}
