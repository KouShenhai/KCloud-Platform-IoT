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

package org.laokou.common.vertx.model;

import lombok.Getter;

/**
 * http消息枚举.
 *
 * @author laokou
 */
@Getter
public enum HttpMessageEnum {

	UP_PROPERTY_REPORT("up_property_report", "属性上报【上行】") {
		@Override
		public String getRouter() {
			return "/:productId/:deviceId/up/property/report";
		}

		@Override
		public String getMqTopic() {
			return "up-property-report";
		}
	};

	private final String code;

	private final String desc;

	HttpMessageEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract String getRouter();

	public abstract String getMqTopic();

}
