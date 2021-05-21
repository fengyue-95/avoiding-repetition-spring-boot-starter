package com.fengyue95.avoidingrepetitionspringbootstarter.intercepter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class WebMvcConfg implements WebMvcConfigurer {

    @Autowired
    private AvoidingRepetitionIntercepter avoidingRepetitionIntercepter;

    /**
     * @return 登录验证拦截器 自定义登录验证拦截器
     */
    @Bean
    public AvoidingRepetitionIntercepter needLoginInterceptor() {
        return avoidingRepetitionIntercepter;
    }

    /**
     * @param registry 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加登录处理拦截器，拦截所有请求，登录请求除外
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(needLoginInterceptor());
        // 配置拦截策略
        interceptorRegistration.addPathPatterns("/**");
    }

}
