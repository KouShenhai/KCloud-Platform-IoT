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

package org.laokou.auth.event.handler;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.dto.log.domainevent.LoginLogEvent;
import org.laokou.auth.gatewayimpl.database.LoginLogMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.mybatisplus.template.TableTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static org.laokou.auth.common.Constant.LOGIN_LOG;
import static org.laokou.common.i18n.common.Constant.UNDER;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class LoginLogHandler implements ApplicationListener<LoginLogEvent> {

	private final LoginLogMapper loginLogMapper;

	private final ThreadPoolTaskExecutor taskExecutor;

	@Override
	@Async
	public void onApplicationEvent(LoginLogEvent event) {
		CompletableFuture.runAsync(() -> {
			try {
				DynamicDataSourceContextHolder.push(LOGIN_LOG);
				execute(event);
			}
			catch (Exception e) {
				log.error("数据插入失败，错误信息：{}", e.getMessage());
			}
			finally {
				DynamicDataSourceContextHolder.clear();
			}
		}, taskExecutor);
	}

	private void execute(LoginLogEvent event) {
		LoginLogDO logDO = ConvertUtil.sourceToTarget(event, LoginLogDO.class);
		logDO.setCreator(event.getUserId());
		loginLogMapper.insertDynamicTable(logDO, TableTemplate.getLoginLogSqlScript(DateUtil.now()),
				UNDER.concat(DateUtil.format(DateUtil.now(), DateUtil.YYYYMM)));
	}

}
