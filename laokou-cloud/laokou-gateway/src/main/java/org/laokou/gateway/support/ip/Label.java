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

package org.laokou.gateway.support.ip;

import lombok.Getter;

/**
 * 标签枚举.
 *
 * @author laokou
 */
@Getter
public enum Label {

	/**
	 * 白名单.
	 */
	WHITE("white"),

	/**
	 * 黑名单.
	 */
	BLACK("black");

	private final String name;

	Label(String name) {
		this.name = name;
	}

	public static Label getInstance(String code) {
		return Label.valueOf(code);
	}

}
