/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.laokou.common.i18n.common.Constant.EMPTY;

/**
 * @author laokou
 */
public class CollectionUtil {

	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static boolean isEmpty(Collection<?> collection) {
		return CollectionUtils.isEmpty(collection);
	}

	public static String toStr(List<String> list,String on) {
		if (list.isEmpty()) {
			return EMPTY;
		}
		return Joiner.on(on).skipNulls().join(list);
	}

	public static List<String> toList(String str,String on) {
		if (StringUtil.isEmpty(str)) {
			return new ArrayList<>(0);
		}
		return Splitter.on(on).trimResults().splitToList(str);
	}

}
