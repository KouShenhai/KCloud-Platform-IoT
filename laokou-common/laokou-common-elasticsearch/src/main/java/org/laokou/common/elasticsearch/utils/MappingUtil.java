/*
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
package org.laokou.common.elasticsearch.utils;

import org.laokou.common.elasticsearch.annotation.Field;
import org.laokou.common.elasticsearch.enums.FieldTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
public class MappingUtil {

	public static <TDocument> List<Mapping> mappingList(Class<TDocument> clazz) {
		// 获取所有字段（包含私有）
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		List<Mapping> mappingList = new ArrayList<>(fields.length);
		for (java.lang.reflect.Field field : fields) {
			boolean annotationPresent = field.isAnnotationPresent(Field.class);
			if (annotationPresent) {
				Field f = field.getAnnotation(Field.class);
				String fieldName = f.value();
				FieldTypeEnum fieldType = f.type();
				String analyzer = f.analyzer();
				String searchAnalyzer = f.searchAnalyzer();
				mappingList.add(new Mapping(fieldName, fieldType, searchAnalyzer, analyzer));
			}
		}
		return mappingList;
	}

}
