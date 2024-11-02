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

package org.laokou.common.xss.annotation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;
import org.laokou.common.core.annotation.AbstractContextualSerializer;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.xss.util.XssUtil;

/**
 * @author laokou
 */
public class XssSerializer extends AbstractContextualSerializer {

	@Override
	@SneakyThrows
	public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
		jsonGenerator.writeString(XssUtil.clearSql(s));
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) {
		XssSql xssSql = beanProperty.getAnnotation(XssSql.class);
		if (ObjectUtil.isNotNull(xssSql)) {
			return new XssSerializer();
		}
		throw new RuntimeException();
	}

}
