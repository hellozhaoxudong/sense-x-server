package com.sense.app.core.filter;

import com.sense.app.core.system.domain.SysLog;
import com.sense.app.core.system.manager.SysLogAsync;
import com.sense.app.core.utils.CurrentUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class AccessLogFilter extends OncePerRequestFilter {

    @Autowired
    private SysLogAsync sysLogAsync;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();

        SysLog log = new SysLog();
        log.setLogDate(new Date());

        // 方法
        log.setReqMethod(request.getMethod());
        log.setReqUrl(request.getRequestURI());
        log.setUserIp("127.0.0.1");

        // 查询参数
        String queryString = request.getQueryString();
        log.setLogMsg(queryString);

        try {
            filterChain.doFilter(request, response);
        } finally {
            log.setRepStatus(response.getStatus());
            log.setRepCost(System.currentTimeMillis() - start);

            // 用户信息
            try{
                Long userId = CurrentUser.getUserIdNoException();
                log.setUserId(userId);
                log.setUserName(CurrentUser.getUserName());
                log.setTenantId(CurrentUser.getTenantId());
                log.setCreateUser(userId);
                log.setUpdateUser(userId);
            } catch (Exception e){}

            sysLogAsync.saveLog(log);
        }
    }
}
