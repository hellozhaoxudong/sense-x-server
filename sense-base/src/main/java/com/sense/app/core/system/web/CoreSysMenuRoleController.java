package com.sense.app.core.system.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.core.system.domain.CoreSysMenu;
import com.sense.app.core.system.domain.CoreSysMenuRole;
import com.sense.app.core.system.service.CoreSysMenuRoleService;
import com.sense.app.core.utils.PageUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单-角色分配控制器
 * 由租户管理员操作，为租户内的角色分配菜单权限
 * 
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/core/sys/menu/role")
public class CoreSysMenuRoleController {

    @Autowired
    private CoreSysMenuRoleService service;

    /**
     * 分页查询角色菜单分配列表
     * @param roleId 角色ID（可选，用于筛选特定角色的分配情况）
     * @param page 当前页
     * @param pageSize 每页条数
     * @return 分配列表
     */
    @GetMapping("/page")
    public ResponseEntity<List<CoreSysMenuRole>> queryMenuRoleList(
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page mybatisPage = PageUtil.getPage(page, pageSize);
        List<CoreSysMenuRole> list = service.queryMenuRoleList(mybatisPage, roleId);
        return new ResponseEntity(list, PageUtil.getTotalHeader(mybatisPage), HttpStatus.OK);
    }

    /**
     * 查询角色已分配的菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    @GetMapping("/menuIds")
    public ResponseEntity<List<Long>> queryMenuIdsByRoleId(@RequestParam(value = "roleId") Long roleId) {
        List<Long> menuIds = service.queryMenuIdsByRoleId(roleId);
        return new ResponseEntity<>(menuIds, HttpStatus.OK);
    }

    /**
     * 查询菜单已分配的角色ID列表
     * @param menuId 菜单ID
     * @return 角色ID列表
     */
    @GetMapping("/roleIds")
    public ResponseEntity<List<Long>> queryRoleIdsByMenuId(@RequestParam(value = "menuId") Long menuId) {
        List<Long> roleIds = service.queryRoleIdsByMenuId(menuId);
        return new ResponseEntity<>(roleIds, HttpStatus.OK);
    }

    /**
     * 为角色分配菜单权限
     * 前端传入角色ID和菜单ID列表
     * 
     * @param request 分配请求（包含roleId和menuIds）
     * @return 操作结果
     */
    @PostMapping("/assign")
    public ResponseEntity<Boolean> assignMenusToRole(@RequestBody AssignMenuRequest request) {
        service.assignMenusToRole(request.getRoleId(), request.getMenuIds());
        return ResponseEntity.ok(true);
    }

    /**
     * 批量为多个角色分配相同的菜单权限
     * 前端传入角色ID列表和菜单ID列表
     * 
     * @param request 批量分配请求（包含roleIds和menuIds）
     * @return 操作结果
     */
    @PostMapping("/assignBatch")
    public ResponseEntity<Boolean> assignMenusToRoles(@RequestBody AssignMenuBatchRequest request) {
        service.assignMenusToRoles(request.getRoleIds(), request.getMenuIds());
        return ResponseEntity.ok(true);
    }

    /**
     * 从角色移除指定菜单权限
     * @param request 移除请求（包含roleId和menuIds）
     * @return 操作结果
     */
    @PostMapping("/remove")
    public ResponseEntity<Boolean> removeMenusFromRole(@RequestBody AssignMenuRequest request) {
        service.removeMenusFromRole(request.getRoleId(), request.getMenuIds());
        return ResponseEntity.ok(true);
    }

    /**
     * 清空角色的所有菜单权限
     * @param roleId 角色ID
     * @return 操作结果
     */
    @PostMapping("/clear")
    public ResponseEntity<Boolean> clearRoleMenus(@RequestParam(value = "roleId") Long roleId) {
        service.clearRoleMenus(roleId);
        return ResponseEntity.ok(true);
    }

    /**
     * 查询角色已分配的菜单树形结构（包含菜单别名）
     * @param roleId 角色ID
     * @return 菜单树（带menuAliasName）
     */
    @GetMapping("/menuTree")
    public ResponseEntity<List<CoreSysMenu>> queryRoleMenuTree(@RequestParam(value = "roleId") Long roleId) {
        List<CoreSysMenu> tree = service.queryRoleMenuTree(roleId);
        return new ResponseEntity<>(tree, HttpStatus.OK);
    }

    /**
     * 批量更新角色菜单别名
     * @param request 请求对象（包含 roleId 和 aliasList）
     * @return 操作结果
     */
    @PostMapping("/updateAlias")
    public ResponseEntity<Boolean> updateRoleMenuAlias(@RequestBody UpdateAliasRequest request) {
        service.updateRoleMenuAlias(request.getRoleId(), request.getAliasList());
        return ResponseEntity.ok(true);
    }

    /**
     * 分配菜单请求对象
     */
    @Data
    public static class AssignMenuRequest {
        /**
         * 角色ID
         */
        private Long roleId;
        
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
         * 角色ID列表
         */
        private List<Long> roleIds;
        
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
         * 角色ID
         */
        private Long roleId;

        /**
         * 别名列表（每项包含 menuId 和 menuAliasName）
         */
        private List<CoreSysMenuRole> aliasList;
    }
}
