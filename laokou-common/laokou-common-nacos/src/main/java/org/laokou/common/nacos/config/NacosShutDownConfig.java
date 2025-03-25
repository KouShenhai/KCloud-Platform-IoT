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

package org.laokou.common.nacos.config;

import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.nacos.util.NamingUtils;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class NacosShutDownConfig {

	private final NamingUtils namingUtils;

	@PreDestroy
	public void preDestroy() throws NacosException {
		// 服务下线
		log.info("开始执行服务下线");
		namingUtils.nacosServiceShutDown();
		log.info("执行服务下线完成");
	}

}
