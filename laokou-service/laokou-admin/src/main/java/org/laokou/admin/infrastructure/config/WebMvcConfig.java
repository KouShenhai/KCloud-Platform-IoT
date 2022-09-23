/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.infrastructure.config;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.laokou.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.unit.DataSize;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.MultipartConfigElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
/**
 * web mvc配置
 * @author Kou Shenhai
 */
@Configuration
@Slf4j
public class WebMvcConfig{

    /**
     * SimpleDateFormat线程不安全
     */
    private static final ThreadLocal<DateFormat> df = ThreadLocal.withInitial(() -> new SimpleDateFormat(DateUtil.DATE_TIME_PATTERN));

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有域名跨域
        config.addAllowedOriginPattern(CorsConfiguration.ALL);
        // 允许证书
        config.setAllowCredentials(true);
        // 允许所有方法
        config.addAllowedMethod(CorsConfiguration.ALL);
        // 允许任何头
        config.addAllowedHeader(CorsConfiguration.ALL);
        // 每一个小时，异步请求都发起预检请求 => 发送两次请求 第一次OPTION 第二次GET/POT/PUT/DELETE
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",config);
        return new CorsFilter(configurationSource);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(999 * 1024 * 1024 * 1024)); // 限制上传文件大小
        return factory.createMultipartConfig();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        //日期格式转换
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        mapper.setDateFormat(df.get());
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //Long类型转String类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
        javaTimeModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
        mapper.registerModule(javaTimeModule);
        converter.setObjectMapper(mapper);
        return converter;
    }

}
