package com.kingbom.linebot.config;

import com.kingbom.linebot.LinebotApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Created by bombay on 13/8/2018 AD.
 */
@Slf4j
@Configuration
public class WebConfigure implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String downloadedContentUri = LinebotApplication.downloadedContentDir.toUri().toASCIIString();
        registry.addResourceHandler("/downloaded/**").addResourceLocations(downloadedContentUri);
    }

}
