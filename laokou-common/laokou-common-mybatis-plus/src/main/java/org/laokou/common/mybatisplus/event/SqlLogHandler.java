/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.mybatisplus.event;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.mybatisplus.database.SqlLogMapper;
import org.laokou.common.mybatisplus.database.dataobject.SqlLogDO;
import org.laokou.common.mybatisplus.handler.SqlLogEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.concurrent.CompletableFuture;
import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class SqlLogHandler implements ApplicationListener<SqlLogEvent> {

	private final SqlLogMapper sqlLogMapper;

	private final ThreadPoolTaskExecutor taskExecutor;

	@Override
	public void onApplicationEvent(SqlLogEvent event) {
		CompletableFuture.runAsync(() -> {
			try {
				execute(event);
			}
			catch (Exception e) {
				log.error("数据插入失败，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
			}
		}, taskExecutor);
	}

	private void execute(SqlLogEvent event) {
		try {
			DynamicDataSourceContextHolder.push(MASTER);
			sqlLogMapper.insertTable(toSqlLog(event));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private SqlLogDO toSqlLog(SqlLogEvent event) {
		SqlLogDO sqlLogDO = ConvertUtil.sourceToTarget(event, SqlLogDO.class);
		Assert.isTrue(ObjectUtil.isNotNull(sqlLogDO), "sqlLogDO is null");
		sqlLogDO.setDeptId(1535887940687765505L);
		sqlLogDO.setDeptPath("0,1535887940687765505");
		sqlLogDO.setEditor(1707428076142559234L);
		sqlLogDO.setCreator(1707428076142559234L);
		return sqlLogDO;
	}

}
