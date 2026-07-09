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

package org.laokou.iot.source.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.source.gateway.SourceGateway;
import org.laokou.iot.source.model.entity.SourceE;
import org.springframework.stereotype.Component;

/**
 * 数据源领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceDomainService {

	private final SourceGateway sourceGateway;

	public void createSource(SourceE sourceE) {
		checkConnection(sourceE);
		sourceGateway.createSource(sourceE);
	}

	public void updateSource(SourceE sourceE) {
		checkConnection(sourceE);
		sourceGateway.updateSource(sourceE);
	}

	public void deleteSource(Long[] ids) {
		sourceGateway.deleteSource(ids);
	}

	public void testConnection(SourceE sourceE) {
		checkConnection(sourceE);
	}

	private void checkConnection(SourceE sourceE) {
		// if ("com.taosdata.jdbc.rs.RestfulDriver".equals(sourceE.getDriverClassName()))
		// {
		// try {
		// Class.forName(sourceE.getDriverClassName());
		// DriverManager.setLoginTimeout(5);
		// try (Connection ignored = DriverManager.getConnection(sourceE.getUrl(),
		// sourceE.getUsername(),
		// sourceE.getPassword())) {
		// // 连接成功
		// }
		// }
		// catch (ClassNotFoundException | SQLException e) {
		// throw new SystemException("SOURCE_CONNECTION_FAILED", "TDengine 数据源连接失败：" +
		// e.getMessage(), e);
		// }
		// }
	}

}
