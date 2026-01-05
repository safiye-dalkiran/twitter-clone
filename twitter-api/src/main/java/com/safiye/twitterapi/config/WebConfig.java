package com.safiye.twitterapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Tüm API yollarına izin ver
                .allowedOrigins("http://localhost:3200") // Sadece React projesine izin ver
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // İzin verilen metodlar
                .allowedHeaders("*") // Tüm header bilgilerine izin ver
                .allowCredentials(true); // Cookie veya Auth bilgilerine izin ver
    }
}