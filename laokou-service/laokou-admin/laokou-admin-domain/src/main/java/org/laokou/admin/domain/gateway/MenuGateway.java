/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.domain.gateway;

import org.laokou.admin.domain.menu.Menu;
import org.laokou.admin.domain.user.User;

import java.util.List;

/**
 * @author laokou
 */
public interface MenuGateway {

	List<Menu> list(User user, Integer type);

	Boolean update(Menu menu);

	Boolean insert(Menu menu);

	Boolean deleteById(Long id);

	Menu getById(Long id);

	List<Long> getIdsByRoleId(Long roleId);

	List<Menu> list(Menu menu, Long tenantId);

	List<Menu> getTenantMenuList();

}
