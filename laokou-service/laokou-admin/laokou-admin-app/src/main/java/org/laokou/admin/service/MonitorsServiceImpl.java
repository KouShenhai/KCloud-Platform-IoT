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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.MonitorsServiceI;
import org.laokou.admin.dto.monitor.MonitorCacheGetQry;
import org.laokou.admin.dto.monitor.MonitorServerGetQry;
import org.laokou.admin.dto.monitor.clientobject.CacheCO;
import org.laokou.admin.dto.monitor.clientobject.ServerCO;
import org.laokou.admin.command.monitor.query.MonitorCacheGetQryExe;
import org.laokou.admin.command.monitor.query.MonitorServerGetQryExe;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class MonitorsServiceImpl implements MonitorsServiceI {

	private final MonitorCacheGetQryExe monitorCacheGetQryExe;

	private final MonitorServerGetQryExe monitorServerGetQryExe;

	@Override
	public Result<CacheCO> cache(MonitorCacheGetQry qry) {
		return monitorCacheGetQryExe.execute(qry);
	}

	@Override
	public Result<ServerCO> server(MonitorServerGetQry qry) {
		return monitorServerGetQryExe.execute(qry);
	}

}
