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
package org.laokou.common.i18n.utils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#context-functionality-messagesource
 * @author laokou
 */
public class MessageUtil {

    private static final ReloadableResourceBundleMessageSource resourceBundleMessageSource;

    static {
        resourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        resourceBundleMessageSource.setBasename("classpath:i18n/message");
    }

    public static String getMessage(int code) {
        return resourceBundleMessageSource.getMessage("" + code, new String[0], Locale.CHINA);
    }

    public static void main(String[] args) {
        System.out.println(getMessage(200));
    }

}
