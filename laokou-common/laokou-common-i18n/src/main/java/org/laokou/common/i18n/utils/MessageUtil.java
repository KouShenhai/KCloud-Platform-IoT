/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
 *
 */

package org.laokou.common.i18n.utils;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

/**
 * 国际化消息工具类. <a href=
 * "https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#context-functionality-messagesource">...</a>
 *
 * @author laokou
 */
public final class MessageUtil {

	private static final ReloadableResourceBundleMessageSource RESOURCE_BUNDLE_MESSAGE_SOURCE;

	static {
		RESOURCE_BUNDLE_MESSAGE_SOURCE = new ReloadableResourceBundleMessageSource();
		RESOURCE_BUNDLE_MESSAGE_SOURCE.setDefaultEncoding(StandardCharsets.UTF_8.name());
		RESOURCE_BUNDLE_MESSAGE_SOURCE.setBasename("classpath:i18n/message");
	}

	private MessageUtil() {
	}

	public static String getMessage(String code) {
		return getMessage(code, null);
	}

	public static String getMessage(String code, Object[] args) {
		return RESOURCE_BUNDLE_MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale());
	}

}
