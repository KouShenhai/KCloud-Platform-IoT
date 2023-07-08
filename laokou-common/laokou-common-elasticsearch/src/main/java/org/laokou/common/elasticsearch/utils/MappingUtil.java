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
package org.laokou.common.elasticsearch.utils;

import org.laokou.common.elasticsearch.annotation.EsField;
import org.laokou.common.elasticsearch.enums.FieldTypeEnum;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
public class MappingUtil {

	public static <TDocument> List<Mapping> mappingList(Class<TDocument> clazz) {
		// 获取所有字段（包含私有）
		Field[] fields = clazz.getDeclaredFields();
		List<Mapping> mappingList = new ArrayList<>(fields.length);
		for (Field field : fields) {
			boolean annotationPresent = field.isAnnotationPresent(EsField.class);
			if (annotationPresent) {
				EsField esField = field.getAnnotation(EsField.class);
				String fieldName = esField.value();
				FieldTypeEnum fieldType = esField.type();
				String analyzer = esField.analyzer();
				String searchAnalyzer = esField.searchAnalyzer();
				mappingList.add(new Mapping(fieldName, fieldType, searchAnalyzer, analyzer));
			}
		}
		return mappingList;
	}

}
