package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot 应用配置
 */
@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {
    // 可在此添加 Bean 定义
}
