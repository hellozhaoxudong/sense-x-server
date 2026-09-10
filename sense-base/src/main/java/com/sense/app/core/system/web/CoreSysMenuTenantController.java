package com.sense.app.core.system.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.core.system.domain.CoreSysMenu;
import com.sense.app.core.system.domain.CoreSysMenuTenant;
import com.sense.app.core.system.service.CoreSysMenuTenantService;
import com.sense.app.core.utils.CurrentUser;
import com.sense.app.core.utils.PageUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单-租户分配控制器
 * 由超级管理员操作，用于给其他租户分配菜单权限
 * 
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/core/sys/menu/tenant")
public class CoreSysMenuTenantController {

    @Autowired
    private CoreSysMenuTenantService service;

    /**
     * 分页查询租户菜单分配列表
     * @param tenantId 租户ID（可选，用于筛选特定租户的分配情况）
     * @param page 当前页
     * @param pageSize 每页条数
     * @return 分配列表
     */
    @GetMapping("/page")
    public ResponseEntity<List<CoreSysMenuTenant>> queryMenuTenantList(
            @RequestParam(value = "tenantId", required = false) Long tenantId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);
        List<CoreSysMenuTenant> list = service.queryMenuTenantList(mybatisPage, tenantId);
        return new ResponseEntity(list, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 查询租户已分配的菜单ID列表
     * @param tenantId 租户ID
     * @return 菜单ID列表
     */
    @GetMapping("/menuIds")
    public ResponseEntity<List<Long>> queryMenuIdsByTenantId(@RequestParam(value = "tenantId") Long tenantId) {
        List<Long> menuIds = service.queryMenuIdsByTenantId(tenantId);
        return new ResponseEntity<>(menuIds, HttpStatus.OK);
    }

    /**
     * 查询菜单已分配的租户ID列表
     * @param menuId 菜单ID
     * @return 租户ID列表
     */
    @GetMapping("/tenantIds")
    public ResponseEntity<List<Long>> queryTenantIdsByMenuId(@RequestParam(value = "menuId") Long menuId) {
        List<Long> tenantIds = service.queryTenantIdsByMenuId(menuId);
        return new ResponseEntity<>(tenantIds, HttpStatus.OK);
    }

    /**
     * 为租户分配菜单权限
     * 前端传入租户ID和菜单ID列表
     * 
     * @param request 分配请求（包含tenantId和menuIds）
     * @return 操作结果
     */
    @PostMapping("/assign")
    public ResponseEntity<Boolean> assignMenusToTenant(@RequestBody AssignMenuRequest request) {
        service.assignMenusToTenant(request.getTenantId(), request.getMenuIds());
        return ResponseEntity.ok(true);
    }

    /**
     * 批量为多个租户分配相同的菜单权限
     * 前端传入租户ID列表和菜单ID列表
     * 
     * @param request 批量分配请求（包含tenantIds和menuIds）
     * @return 操作结果
     */
    @PostMapping("/assignBatch")
    public ResponseEntity<Boolean> assignMenusToTenants(@RequestBody AssignMenuBatchRequest request) {
        service.assignMenusToTenants(request.getTenantIds(), request.getMenuIds());
        return ResponseEntity.ok(true);
    }

    /**
     * 从租户移除指定菜单权限
     * @param request 移除请求（包含tenantId和menuIds）
     * @return 操作结果
     */
    @PostMapping("/remove")
    public ResponseEntity<Boolean> removeMenusFromTenant(@RequestBody AssignMenuRequest request) {
        service.removeMenusFromTenant(request.getTenantId(), request.getMenuIds());
        return ResponseEntity.ok(true);
    }

    /**
     * 清空租户的所有菜单权限
     * @param tenantId 租户ID
     * @return 操作结果
     */
    @PostMapping("/clear")
    public ResponseEntity<Boolean> clearTenantMenus(@RequestParam(value = "tenantId") Long tenantId) {
        service.clearTenantMenus(tenantId);
        return ResponseEntity.ok(true);
    }

    /**
     * 查询租户已分配的菜单树形结构（包含菜单别名）
     * 如果不传tenantId，则使用当前登录用户的租户ID
     * @param tenantId 租户ID（可选）
     * @return 菜单树（带menuAliasName）
     */
    @GetMapping("/menuTree")
    public ResponseEntity<List<CoreSysMenu>> queryTenantMenuTree(@RequestParam(value = "tenantId", required = false) Long tenantId) {
        if (tenantId == null) {
            tenantId = CurrentUser.getTenantId();
        }
        List<CoreSysMenu> tree = service.queryTenantMenuTree(tenantId);
        return new ResponseEntity<>(tree, HttpStatus.OK);
    }

    /**
     * 批量更新租户菜单别名
     * @param request 请求对象（包含 tenantId 和 aliasList）
     * @return 操作结果
     */
    @PostMapping("/updateAlias")
    public ResponseEntity<Boolean> updateMenuAlias(@RequestBody UpdateAliasRequest request) {
        service.updateMenuAlias(request.getTenantId(), request.getAliasList());
        return ResponseEntity.ok(true);
    }

    /**
     * 分配菜单请求对象
     */
    @Data
    public static class AssignMenuRequest {
        /**
         * 租户ID（前端传入，非当前登录用户的租户）
         */
        private Long tenantId;
        
        /**
         * 菜单ID列表
         */
        private List<Long> menuIds;
    }

    /**
     * 批量分配菜单请求对象
     */
    @Data
    public static class AssignMenuBatchRequest {
        /**
         * 租户ID列表
         */
        private List<Long> tenantIds;
        
        /**
         * 菜单ID列表
         */
        private List<Long> menuIds;
    }

    /**
     * 更新菜单别名请求对象
     */
    @Data
    public static class UpdateAliasRequest {
        /**
         * 租户ID
         */
        private Long tenantId;

        /**
         * 别名列表（每项包含 menuId 和 menuAliasName）
         */
        private List<CoreSysMenuTenant> aliasList;
    }
}
