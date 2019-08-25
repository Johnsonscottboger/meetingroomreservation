package com.aning.meetingroomreservation.config

import com.aning.meetingroomreservation.interceptor.JwtInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * 拦截器配置
 */
@Configuration
public class InterceptorConfig: WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        val r = registry.addInterceptor(JwtInterceptor())
        r.addPathPatterns("/**")
        super.addInterceptors(registry)
    }
}