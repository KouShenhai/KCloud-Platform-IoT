/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.swagger.config;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.DateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
/**
 * @author laokou
 */
@Configuration
@Slf4j
public class CorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
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
        log.info("cors加载完毕");
        return new CorsFilter(configurationSource);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        //日期格式转换
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        mapper.setDateFormat(new SimpleDateFormat(DateUtil.DATE_TIME_PATTERN));
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //Long类型转String类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
        javaTimeModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
        // 中文转换
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(list);
        mapper.registerModule(javaTimeModule);
        converter.setObjectMapper(mapper);
        log.info("jackson配置加载完毕");
        return converter;
    }

}
