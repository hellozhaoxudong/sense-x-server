package com.sense.app.core.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.system.domain.CoreSysConfig;
import com.sense.app.core.system.mapper.CoreSysConfigMapper;
import com.sense.app.core.utils.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统配置 Service
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreSysConfigService extends ServiceImpl<CoreSysConfigMapper, CoreSysConfig> {

    /**
     * 查询系统配置列表（不分页，按租户隔离）
     * @param configKey 配置KEY（可选，模糊查询）
     * @return 配置列表
     */
    public List<CoreSysConfig> queryList(String configKey) {
        List<String> configKeys = new ArrayList<>();
        if (StrUtil.isNotBlank(configKey)) {
            configKeys = StrUtil.split(configKey, ",");
        }

        LambdaQueryWrapper<CoreSysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollUtil.isNotEmpty(configKeys), CoreSysConfig::getConfigKey, configKeys)
                    .orderByAsc(CoreSysConfig::getConfigKey);
        return list(queryWrapper);
    }


    public Map<String, String> queryConfigMap(String configKey){
        List<CoreSysConfig> result = queryList(configKey);

        return result.stream().collect(Collectors.toMap(CoreSysConfig::getConfigKey,CoreSysConfig::getConfigValue, (k1, k2) -> k2));
    }

    /**
     * 提交系统配置（根据configKey是否存在判断新增或更新，按租户隔离）
     * @param data 配置信息
     * @return 配置ID
     */
    public Long submit(CoreSysConfig data) {
        Long tenantId = CurrentUser.getTenantId();
        data.setTenantId(tenantId);

        LambdaQueryWrapper<CoreSysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoreSysConfig::getConfigKey, data.getConfigKey());
        CoreSysConfig exist = getOne(queryWrapper);

        if (exist != null) {
            data.setId(exist.getId());
            updateById(data);
        } else {
            data.setId(null);
            save(data);
        }
        return data.getId();
    }

}
