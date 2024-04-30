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

package org.laokou.test.container.util;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.ObjectUtil;

@Slf4j
public class ObjectTest {
	public static void main(String[] args) {
		long a = 0;
		Long b = 0L;
		int c = 0;
		Integer d = 0;
		log.info("{}", ObjectUtil.equals(a, b));
		log.info("{}", ObjectUtil.equals((long)c, b));
		log.info("{}", ObjectUtil.equals(c, d));
	}
}
