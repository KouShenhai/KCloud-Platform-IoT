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

package org.laokou.admin.i18nMenu.gateway;

import org.laokou.admin.i18nMenu.model.I18nMenuE;

/**
 * 国际化菜单网关【防腐】.
 *
 * @author laokou
 */
public interface I18nMenuGateway {

	/**
	 * 新增国际化菜单.
	 */
	void createI18nMenu(I18nMenuE i18nMenuE);

	/**
	 * 修改国际化菜单.
	 */
	void updateI18nMenu(I18nMenuE i18nMenuE);

	/**
	 * 删除国际化菜单.
	 */
	void deleteI18nMenu(Long[] ids);

}
