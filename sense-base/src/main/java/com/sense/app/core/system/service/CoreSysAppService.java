package com.sense.app.core.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.system.domain.CoreSysApp;
import com.sense.app.core.system.domain.CoreSysMenu;
import com.sense.app.core.system.domain.CoreSysMenuTenant;
import com.sense.app.core.system.mapper.CoreSysAppMapper;
import com.sense.app.core.system.mapper.CoreSysMenuMapper;
import com.sense.app.core.system.mapper.CoreSysMenuTenantMapper;
import com.sense.app.core.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统应用 Service
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreSysAppService extends ServiceImpl<CoreSysAppMapper, CoreSysApp> {

    @Autowired
    private CoreSysMenuMapper menuMapper;

    @Autowired
    private CoreSysMenuTenantMapper menuTenantMapper;

    /**
     * 查询系统应用列表（不分页）
     * @param appCode 应用编码（可选，模糊查询）
     * @param appName 应用名称（可选，模糊查询）
     * @return 应用列表
     */
    public List<CoreSysApp> queryData(String appCode, String appName) {
        return list(new LambdaQueryWrapper<CoreSysApp>()
                .like(StrUtil.isNotBlank(appCode), CoreSysApp::getAppCode, appCode)
                .like(StrUtil.isNotBlank(appName), CoreSysApp::getAppName, appName)
                .orderByAsc(CoreSysApp::getSortOrder)
                .orderByAsc(CoreSysApp::getId));
    }

    /**
     * 提交系统应用（新增或更新）
     * @param data 应用信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitData(CoreSysApp data) {
        saveOrUpdate(data);
    }

    /**
     * 删除系统应用
     * @param ids 应用ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteData(List<Long> ids) {
        removeByIds(ids);
    }

}
