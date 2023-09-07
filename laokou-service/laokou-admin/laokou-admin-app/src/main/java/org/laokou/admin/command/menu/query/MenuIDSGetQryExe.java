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

package org.laokou.admin.command.menu.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.menu.MenuIDSGetQry;
import org.laokou.admin.domain.gateway.MenuGateway;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuIDSGetQryExe {

	private final MenuGateway menuGateway;

	public Result<List<Long>> execute(MenuIDSGetQry qry) {
		return Result.of(menuGateway.getIdsByRoleId(qry.getRoleId()));
	}

}
