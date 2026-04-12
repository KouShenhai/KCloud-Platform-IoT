/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.common.enums;

import lombok.Getter;
import org.laokou.common.i18n.util.EnumParser;

/**
 * @author laokou
 */
@Getter
public enum BizType {

	AUTH("auth", "认证"),

	USER("user", "用户"),

	DEPT("dept", "部门"),

	ROLE("role", "角色"),

	MENU("menu", "菜单"),

	DICT("dict", "字典"),

	OPERATE_LOG("operate_log", "操作日志"),

	I18N_MENU("i18n_menu", "国际化菜单"),

	THING_MODEL("thing_model", "物模型"),

	PRODUCT_CATEGORY("product_category", "产品类别");

	private final String code;

	private final String desc;

	BizType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static BizType getByCode(String code) {
		return EnumParser.parse(BizType.class, BizType::getCode, code);
	}

}
