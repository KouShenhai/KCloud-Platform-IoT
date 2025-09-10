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

package org.laokou.admin.menu.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.gateway.MenuGateway;
import org.laokou.admin.menu.model.MenuE;
import org.springframework.stereotype.Component;

/**
 * 菜单领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuDomainService {

	private final MenuGateway menuGateway;

	public void createMenu(MenuE menuE) {
		menuE.checkMenuParam();
		menuGateway.createMenu(menuE);
	}

	public void updateMenu(MenuE menuE) {
		menuE.checkMenuParam();
		menuGateway.updateMenu(menuE);
	}

	public void deleteMenu(Long[] ids) {
		menuGateway.deleteMenu(ids);
	}

}
