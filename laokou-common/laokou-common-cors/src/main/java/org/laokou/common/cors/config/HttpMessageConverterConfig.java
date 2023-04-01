/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.cors.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.DateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author laokou
 */
@Configuration
@Slf4j
public class HttpMessageConverterConfig {

    @Bean("jackson2HttpMessageConverter")
    @Order(Ordered.LOWEST_PRECEDENCE - 10000)
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        // 时区
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.getTimePattern(DateUtil.YYYY_MM_DD_HH_MM_SS));
        simpleDateFormat.setTimeZone(timeZone);
        mapper.setDateFormat(simpleDateFormat);
        mapper.setTimeZone(timeZone);
        // Long类型转String类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
        javaTimeModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
        // 中文转换
        List<MediaType> list = new ArrayList<>(1);
        list.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(list);
        mapper.registerModule(javaTimeModule);
        converter.setObjectMapper(mapper);
        log.info("jackson配置加载完毕");
        return converter;
    }

    public static void main(String[] args) {
        StdDateFormat stdDateFormat = new StdDateFormat();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        stdDateFormat.setTimeZone(timeZone);
        stdDateFormat.withLocale(Locale.SIMPLIFIED_CHINESE);
        String format = stdDateFormat.format(new Date());
        System.out.println(format);
        System.out.println(new SimpleDateFormat(DateUtil.getTimePattern(DateUtil.YYYY_MM_DD_HH_MM_SS)).format(new Date()));
    }

}
