package com.example.common.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JWTInterceptor jwtInterceptor;

    @Override
    //注册拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login", "/register", "/files/**");
    }
    //将jwtInterceptor注册为全局拦截器，并对所有路径（"/**"）应用这个拦截器。然后，使用.excludePathPatterns()方法排除不需要拦截的路径：
    //"/"：排除根路径，通常用于主页或欢迎页面。
    //"/login"：排除登录接口，因为用户在登录之前还没有Token。
    //"/register"：排除注册接口，允许用户注册新账号。
    //"/files/**"：排除所有/files下的路径，可能用于静态文件或文件上传服务，这些服务不需要Token验证

}
