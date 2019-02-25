package com.wjclovejava.demo.mini.api.config;

import org.springframework.context.annotation.Configuration;
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
}
