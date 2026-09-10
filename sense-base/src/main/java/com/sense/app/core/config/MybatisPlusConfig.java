package com.sense.app.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.sense.app.core.utils.CurrentUser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName MybatisPlusConfig
 * @description MybatisPlus多租户配置
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Configuration
public class MybatisPlusConfig {


    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 多租户配置
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                Long tenantId = CurrentUser.getTenantId();
                return new LongValue(tenantId);
            }

            @Override
            public boolean ignoreTable(String tableName) {
                List<String> tables = new ArrayList<String>();
                tables.add("sys_user");
                tables.add("sys_menu");

                if (tables.contains(tableName)){
                    return true;
                }

                return false;
            }
        }));

        // 分页插件配置
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return interceptor;
    }
}
