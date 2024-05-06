/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.sensitive.annotation.SensitiveField;

import java.lang.reflect.Field;

import static org.laokou.common.i18n.common.constants.StringConstant.*;

/**
 * @author laokou
 */
public class SensitiveUtil {

	public static void transform(Object obj) throws IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean annotationPresent = field.isAnnotationPresent(SensitiveField.class);
			if (annotationPresent) {
				// 私有属性
				field.setAccessible(true);
				Object o = field.get(obj);
				if (ObjectUtil.isNull(o)) {
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

	public static String format(Type type, String str) {
		return switch (type) {
			case MAIL -> formatMail(str);
			case MOBILE -> formatMobile(str);
		};
	}

	private static String formatMail(String mail) {
		if (StringUtil.isEmpty(mail)) {
			return EMPTY;
		}
		int index = mail.indexOf(AT);
		if (index == -1) {
			return mail;
		}
		String begin = mail.substring(0, 1);
		String end = mail.substring(index);
		return begin + START_START + START_START + end;
	}

	private static String formatMobile(String mobile) {
		if (StringUtil.isEmpty(mobile)) {
			return EMPTY;
		}
		int mobileLen = 11;
		if (mobile.length() != mobileLen) {
			return mobile;
		}
		String begin = mobile.substring(0, 3);
		String end = mobile.substring(7);
		return begin + START_START + START_START + end;
	}

}
