package com.wjclovejava.demo.mini.api.config;

import com.wjclovejava.demo.mini.api.controller.interceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/22 14:47
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                //访问swagger-ui
                .addResourceLocations("classpath:META-INF/resources/")
                //访问图片
                .addResourceLocations("file:F:/video_data/");
    }


    @Bean
    public MiniInterceptor miniInterceptor() {
        return new MiniInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
                .addPathPatterns("/video/upload", "/video/uploadCover",
                        "/video/userLike", "/video/userUnLike",
                        "/video/saveComment")
                .addPathPatterns("/bgm/**")
                .excludePathPatterns("/user/queryPublisher");

        super.addInterceptors(registry);
    }
}
