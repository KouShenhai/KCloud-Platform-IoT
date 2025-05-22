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

package org.laokou.common.data.cache.model;

import lombok.Getter;

/**
 * @author laokou
 */
@Getter
public enum MqEnum {

	CACHE("cache", "缓存") {
		@Override
		public String getTopic() {
			return CACHE_TOPIC;
		}
	};

	private final String code;

	private final String desc;

	MqEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract String getTopic();

	public static final String CACHE_CONSUMER_GROUP = "laokou_cache_consumer_group";

	public static final String CACHE_TOPIC = "laokou_cache_topic";

}
