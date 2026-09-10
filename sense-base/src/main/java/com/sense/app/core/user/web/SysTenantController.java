package com.sense.app.core.user.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sense.app.core.user.domain.SysTenant;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.service.SysTenantService;
import com.sense.app.core.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysTenantController
 * @description 租户管理接口
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/core/tenant")
public class SysTenantController {

    @Autowired
    private SysTenantService service;

    /**
     * queryTenants : 查询租户列表信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param tenantName    租户名称
     * @param companyName   公司名称
     * @param enabled       启用状态
     * @param page          当前页
     * @param pageSize      每页条数
     */
    @GetMapping("/query")
    public ResponseEntity<List<SysTenant>> queryTenants(@RequestParam(value = "tenantName", required = false) String tenantName,
                                                        @RequestParam(value = "companyName", required = false) String companyName,
                                                        @RequestParam(value = "enabled", required = false) Integer enabled,
                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page mybatisPage = PageUtil.getPage(page, pageSize);

        List<SysTenant> sysTenants = service.queryTenants(mybatisPage, tenantName, companyName, enabled);
        return new ResponseEntity(sysTenants, PageUtil.getTotalHeader(mybatisPage),HttpStatus.OK);
    }

    /**
     * 查询租户管理员信息
     * @param tenantId  租户ID
     * @return
     */
    @GetMapping("/admin/user")
    public ResponseEntity<SysUser> queryTenantAdminUser(@RequestParam(value = "tenantId") Long tenantId){

        return new ResponseEntity(service.queryTenantAdminUser(tenantId), HttpStatus.OK);
    }

    /**
     * 创建一个新租户
     * @param tenant
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    @PostMapping("/create")
    public ResponseEntity createTenant(@RequestBody SysTenant tenant){

        service.createTenant(tenant);
        return ResponseEntity.ok(true);
    }

    /**
     * submitTenant : 提交租户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param data 租户信息
     */
    @PostMapping("/submit")
    public ResponseEntity submitTenant(@RequestBody SysTenant data){

        service.submitTenant(data);
        return ResponseEntity.ok(true);
    }

    /**
     * deleteTenants : 删除租户信息
     *
     * @author sense-x
     * @date 2026-01-01 23:00:00
     * @param ids	租户ID集合
     */
    @PostMapping("/delete")
    public ResponseEntity deleteTenants(@RequestBody List<Long> ids){
        service.deleteTenants(ids);
        return ResponseEntity.ok(true);
    }
}