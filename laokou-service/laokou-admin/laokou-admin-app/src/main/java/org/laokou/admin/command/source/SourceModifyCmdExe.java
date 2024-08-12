/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.source;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.SourceGateway;
import org.laokou.admin.dto.source.SourceModifyCmd;
import org.springframework.stereotype.Component;

/**
 * 修改数据源执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceModifyCmdExe {

	private final SourceGateway sourceGateway;

	/**
	 * 执行修改数据源.
	 * @param cmd 修改数据源
	 */
	public void executeVoid(SourceModifyCmd cmd) {
		// sourceGateway.modify(sourceConvertor.toEntity(cmd.getSourceCO()));
	}

}
