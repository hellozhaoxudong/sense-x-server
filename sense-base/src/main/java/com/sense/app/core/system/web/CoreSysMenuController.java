package com.sense.app.core.system.web;

import com.sense.app.core.system.domain.CoreSysApp;
import com.sense.app.core.system.domain.CoreSysMenu;
import com.sense.app.core.system.service.CoreSysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单管理控制器
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/core/sys/menu")
public class CoreSysMenuController {

    @Autowired
    private CoreSysMenuService service;

    /**
     * 查询菜单树（树形结构）
     * @return 菜单树
     */
    @GetMapping("/tree")
    public ResponseEntity<List<CoreSysMenu>> queryMenuTree(@RequestParam(value = "appId") Long appId) {
        List<CoreSysMenu> tree = service.queryMenuTree(appId);
        return new ResponseEntity<>(tree, HttpStatus.OK);
    }

    /**
     * 查询所有菜单（平铺列表，不分页）
     * @return 所有菜单列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<CoreSysMenu>> queryAllMenus() {
        List<CoreSysMenu> menus = service.queryAllMenus();
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    /**
     * 查询所有菜单树
     * 用户为租户分配菜单时的选择
     */
    @GetMapping("/tree/all")
    public ResponseEntity<List<CoreSysMenu>> queryAllMenuTree() {
        List<CoreSysMenu> list = service.queryAllMenuTree();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 查询租户内菜单树
     * 用户为角色分配菜单时的选择
     */
    @GetMapping("/tree/tenant")
    public ResponseEntity<List<CoreSysMenu>> queryTenantMenuTree() {
        List<CoreSysMenu> list = service.queryTenantMenuTree();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 根据ID查询菜单详情
     * @param id 菜单ID
     * @return 菜单详情
     */
    @GetMapping("/queryById")
    public ResponseEntity<CoreSysMenu> queryById(@RequestParam(value = "id") Long id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    /**
     * 根据父菜单ID查询子菜单
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    @GetMapping("/listByParentId")
    public ResponseEntity<List<CoreSysMenu>> queryByParentId(@RequestParam(value = "parentId") Long parentId) {
        List<CoreSysMenu> menus = service.queryByParentId(parentId);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    /**
     * 提交菜单（新增或更新）
     * @param data 菜单信息
     * @return 菜单ID
     */
    @PostMapping("/submit")
    public ResponseEntity<Long> submitMenu(@RequestBody CoreSysMenu data) {
        service.saveOrUpdate(data);
        return new ResponseEntity<>(data.getId(), HttpStatus.OK);
    }

    /**
     * 删除菜单
     * @param ids 菜单ID列表
     * @return 删除结果
     */
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteMenu(@RequestBody List<Long> ids) {
        service.removeByIds(ids);
        return ResponseEntity.ok(true);
    }
}
