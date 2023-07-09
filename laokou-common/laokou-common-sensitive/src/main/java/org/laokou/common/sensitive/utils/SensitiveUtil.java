/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.common.sensitive.utils;

import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.sensitive.annotation.SensitiveField;
import org.laokou.common.sensitive.enums.TypeEnum;

import java.lang.reflect.Field;

/**
 * @author laokou
 */
public class SensitiveUtil {

	private static final String FILL = "****";

	public static void transform(Object obj) throws IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean annotationPresent = field.isAnnotationPresent(SensitiveField.class);
			if (annotationPresent) {
				// 私有属性
				field.setAccessible(true);
				Object o = field.get(obj);
				if (o == null) {
					continue;
				}
				String data = o.toString();
				SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
				data = format(sensitiveField.type(), data);
				// 属性赋值
				field.set(obj, data);
			}
		}
	}

	public static String format(TypeEnum typeEnum, String str) {
		switch (typeEnum) {
			case MAIL -> {
				return formatMail(str);
			}
			case MOBILE -> {
				return formatMobile(str);
			}
			default -> {
				return "";
			}
		}
	}

	private static String formatMail(String mail) {
		if (StringUtil.isEmpty(mail)) {
			return "";
		}
		int index = mail.indexOf("@");
		if (index == -1) {
			return mail;
		}
		String begin = mail.substring(0, 1);
		String end = mail.substring(index);
		return begin + FILL + end;
	}

	private static String formatMobile(String mobile) {
		if (StringUtil.isEmpty(mobile)) {
			return "";
		}
		if (mobile.length() != 11) {
			return mobile;
		}
		String begin = mobile.substring(0, 3);
		String end = mobile.substring(7);
		return begin + FILL + end;
	}

}
