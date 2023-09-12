/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.admin.api;

import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.dto.menu.*;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * @author laokou
 */
public interface MenusServiceI {

	Result<MenuCO> treeList(MenuTreeListQry qry);

	Result<List<MenuCO>> list(MenuListQry qry);

	Result<MenuCO> getById(MenuGetQry qry);

	Result<Boolean> update(MenuUpdateCmd cmd);

	Result<Boolean> insert(MenuInsertCmd cmd);

	Result<Boolean> deleteById(MenuDeleteCmd cmd);

	Result<MenuCO> tree(MenuTreeGetQry qry);

	Result<List<Long>> ids(MenuIDSGetQry qry);

	Result<MenuCO> tenantTree(MenuTenantTreeGetQry qry);

}
