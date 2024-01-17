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

package org.laokou.common.dynamic.router.utils;

import static org.laokou.common.i18n.common.RouterConstants.GENERATED_NAME_PREFIX;

/**
 * 路由名称工具类.
 *
 * @author laokou
 */
public class NameUtil {

	/**
	 * 路由生成名称.
	 * @param i 索引
	 * @return 路由生成名称
	 */
	public static String generateName(int i) {
		return GENERATED_NAME_PREFIX + i;
	}

}
