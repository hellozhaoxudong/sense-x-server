package com.sense.app.core.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sense.app.core.system.domain.SysThirdApp;
import com.sense.app.core.system.mapper.SysThirdAppMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysThirdAppService extends ServiceImpl<SysThirdAppMapper, SysThirdApp> {

    public List<SysThirdApp> queryList(){
        return list();
    }

    public SysThirdApp queryById(Long id){
        return getById(id);
    }
}
