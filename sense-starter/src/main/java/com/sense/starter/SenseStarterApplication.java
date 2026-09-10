package com.sense.starter;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.sense")
@MapperScan("com.sense.**.mapper")
public class SenseStarterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SenseStarterApplication.class, args);

        Environment env = applicationContext.getEnvironment();
        log.info("""
                   
                   ----------------------------------------------------------
                   \t\
                   Application '{}' is running! Access URLs:
                   \t\
                   Local: \t\thttp://localhost:{}
                   \t\
                   ----------------------------------------------------------""",
                env.getProperty("spring.application.name"),
                CharSequenceUtil.isBlank(env.getProperty("server.port")) ? "8080" : env.getProperty("server.port"));
    }

}
