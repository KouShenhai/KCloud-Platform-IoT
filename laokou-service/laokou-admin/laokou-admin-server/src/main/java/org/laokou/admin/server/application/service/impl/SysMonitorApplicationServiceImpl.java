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
package org.laokou.admin.server.application.service.impl;

import com.alibaba.nacos.shaded.io.grpc.Server;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.vo.CacheVO;
import org.laokou.admin.server.application.service.SysMonitorApplicationService;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysMonitorApplicationServiceImpl implements SysMonitorApplicationService {

	private final RedisUtil redisUtil;

	@Override
	public CacheVO getCacheInfo() {
		CacheVO vo = new CacheVO();
		vo.setCommandStats(redisUtil.getCommandStatus());
		vo.setInfo(redisUtil.getInfo());
		vo.setKeysSize(redisUtil.getKeysSize());
		return vo;
	}

	@Override
	public Server getServerInfo() throws Exception {
		return null;
	}

	// @Override
	// public Server getServerInfo() {
	//// Server server = new Server();
	//// server.copyTo();
	//// return server;
	// }

}
