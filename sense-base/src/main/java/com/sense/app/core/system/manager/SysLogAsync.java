package com.sense.app.core.system.manager;

import com.sense.app.core.system.domain.SysLog;
import com.sense.app.core.system.mapper.SysLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@EnableAsync
public class SysLogAsync {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Async("SystemLogThread")
    public void saveLog(SysLog sysLog){
        try {
            sysLogMapper.insert(sysLog);
        } catch (Exception e){
            e.printStackTrace();
            log.info("### 异步写入日志失败: {}", e.getMessage());
        }
    }
}
