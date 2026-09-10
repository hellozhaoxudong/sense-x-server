package com.sense.app.core.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.system.domain.SysLog;
import com.sense.app.core.system.mapper.SysLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shenfulong
 * @date 2026-01-01 23:00:00
 * @description 用户日志service
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SysLogService extends ServiceImpl<SysLogMapper, SysLog> {

    /**
     *
     * description 用户日志分页查询
     * @param mybatisPage 分页参数
     * @author shenfulong
     * @date 2026-01-01 23:00:00
     */
    public List<SysLog> queryRoles(Page mybatisPage, SysLog log) {
        LambdaQueryWrapper<SysLog> queryWrapper = new LambdaQueryWrapper<>();

        //查询符合条件的记录
        queryWrapper.select()
                .eq(StrUtil.isNotBlank(log.getReqMethod()), SysLog::getReqMethod,log.getReqMethod())
                .like(StrUtil.isNotBlank(log.getUserName()), SysLog::getUserName, log.getUserName())
                .orderByDesc(SysLog::getLogDate);

        // 执行分页查询，返回记录列表
        Page<SysLog> pageResult = page(mybatisPage, queryWrapper);
        return pageResult.getRecords();
    }
}
