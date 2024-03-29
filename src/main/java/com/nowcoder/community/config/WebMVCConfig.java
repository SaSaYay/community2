package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.AlphaInterceptor;
import com.nowcoder.community.controller.interceptor.DataInterceptor;
import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import com.nowcoder.community.controller.interceptor.MessageInterceptor;
import com.nowcoder.community.controller.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author sasayaya
 * @create 2022/7/9 20:03
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

//    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(alphaInterceptor)
            .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
            .addPathPatterns("/register","/login");
    registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
//    registry.addInterceptor(loginRequiredInterceptor)
//                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
    registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
    registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");



    }
}
