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

package org.laokou.common.algorithm.template.select;

import java.util.List;

/**
 * 负载均衡-哈希算法.
 *
 * @author laokou
 */
public class HashSelectAlgorithm extends AbstractSelectAlgorithm {

	/**
	 * 哈希算法.
	 * @param list 集合
	 * @param arg 参数
	 * @param <T> 泛型
	 * @return 实例
	 */
	@Override
	public <T> T select(List<T> list, Object arg) {
		int value = arg.hashCode() % list.size();
		if (value < 0) {
			value = Math.abs(value);
		}
		return list.get(value);
	}

}
