package com.sense.app.core.config;

import com.sense.app.core.security.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration inter = registry.addInterceptor(new AuthInterceptor());
        inter.addPathPatterns("/api/**");
        inter.excludePathPatterns("/api/sense/oauth/**");
    }
}
