/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.beans.BeanUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 对象属性复制工具类.
 *
 * @author laokou
 */
@Slf4j
public class ConvertUtil extends BeanUtils {

	/**
	 * 复制对象.
	 * @param source 源对象
	 * @param target 目标对象
	 * @param <T> 泛型
	 * @return 目标对象
	 */
	public static <T> T sourceToTarget(Object source, Class<T> target) {
		if (ObjectUtil.isNull(source)) {
			return null;
		}
		T targetObject = null;
		try {
			targetObject = instantiateClass(target);
			copyProperties(source, targetObject);
		}
		catch (Exception e) {
			log.error("转换失败", e);
		}
		return targetObject;
	}

	/**
	 * 复制集合.
	 * @param sourceList 源集合
	 * @param target 目标对象
	 * @param <T> 泛型
	 * @return 目标集合
	 */
	public static <T> List<T> sourceToTarget(Collection<?> sourceList, Class<T> target) {
		if (CollectionUtil.isEmpty(sourceList)) {
			return Collections.emptyList();
		}
		return sourceList.stream().map(s -> sourceToTarget(s, target)).toList();
	}

}
