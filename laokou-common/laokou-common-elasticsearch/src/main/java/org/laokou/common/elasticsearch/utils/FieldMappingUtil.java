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

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.elasticsearch.annotation.ElasticsearchField;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 每个属性对应的类型及分词器
 *
 * @author laokou
 */
@Slf4j
public class FieldMappingUtil {

	public static <TDocument> List<FieldMapping> getFieldInfo(Class<TDocument> clazz) {
		// 返回class中的所有字段（包括私有字段）
		Field[] fields = clazz.getDeclaredFields();
		// 创建FieldMapping集合
		List<FieldMapping> fieldMappingList = new ArrayList<>();
		for (Field field : fields) {
			// 获取字段上的FieldInfo对象
			boolean annotationPresent = field.isAnnotationPresent(ElasticsearchField.class);
			if (annotationPresent) {
				ElasticsearchField elasticsearchField = field.getAnnotation(ElasticsearchField.class);
				// 获取字段名称
				String name = field.getName();
				fieldMappingList
					.add(new FieldMapping(name, elasticsearchField.type(), elasticsearchField.participle()));
			}
		}
		return fieldMappingList;
	}

}
