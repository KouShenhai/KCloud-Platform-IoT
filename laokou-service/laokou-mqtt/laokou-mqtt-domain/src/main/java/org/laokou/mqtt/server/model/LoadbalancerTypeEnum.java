/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.mqtt.server.model;

import lombok.Getter;
import org.laokou.common.algorithm.template.Algorithm;
import org.laokou.common.algorithm.template.select.HashAlgorithm;
import org.laokou.common.algorithm.template.select.RandomAlgorithm;
import org.laokou.common.algorithm.template.select.RoundRobinAlgorithm;
import org.laokou.common.i18n.util.EnumParser;

/**
 * @author laokou
 */
@Getter
public enum LoadbalancerTypeEnum {

	HASH("hash", "哈希算法") {
		@Override
		public Algorithm getAlgorithm() {
			return new HashAlgorithm();
		}
	},

	RANDOM("random", "随机算法") {
		public Algorithm getAlgorithm() {
			return new RandomAlgorithm();
		}
	},

	ROUND_ROBIN("round_robin", "轮询算法") {
		public Algorithm getAlgorithm() {
			return new RoundRobinAlgorithm();
		}
	};

	private final String code;

	private final String desc;

	LoadbalancerTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static LoadbalancerTypeEnum getByCode(String code) {
		return EnumParser.parse(LoadbalancerTypeEnum.class, LoadbalancerTypeEnum::getCode, code);
	}

	public abstract Algorithm getAlgorithm();

}
