package com.ecnu.tripmap.config;

import com.ecnu.tripmap.Aspect.UserLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Resource
    private UserLoginInterceptor userLoginInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(userLoginInterceptor).addPathPatterns("/**")
//                .excludePathPatterns("/user/login", "/user/register", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/api"); //fixme
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }


}
