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

package org.laokou.common.tdengine;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.laokou.common.storage.AbstractDataSource;
import org.laokou.common.storage.Config;
import org.laokou.common.storage.Table;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author laokou
 */
public class TDengine extends AbstractDataSource {

	protected JdbcTemplate jdbcTemplate;

	protected HikariDataSource hikariDataSource;

	protected TDengine(Config config) {
		super(config);
	}

	@Override
	public void open() {
		hikariDataSource = getHikariDataSource();
		jdbcTemplate = new JdbcTemplate(hikariDataSource, false);
	}

	@Override
	public void close() {
		hikariDataSource.close();
	}

	@Override
	public void create(Table table) {

	}

	private HikariDataSource getHikariDataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("com.taosdata.jdbc.ws.WebSocketDriver");
		hikariConfig.setJdbcUrl(
				String.format("jdbc:TAOS-WS://%s/%s?varcharAsString=true&batchErrorIgnore=true&timezone=Asia/Shanghai",
						config.getAddress(), config.getDbName()));
		hikariConfig.setUsername(config.getUsername());
		hikariConfig.setPassword(config.getPassword());
		// 线程池名称
		hikariConfig.setPoolName(String.format("iot_tdengine_%d", System.currentTimeMillis()));
		// 关闭自动提交
		hikariConfig.setAutoCommit(false);
		// 连接测试
		hikariConfig.setConnectionTestQuery("SELECT server_status()");
		// 连接最大生命周期(15分钟)
		hikariConfig.setMaxLifetime(900000);
		// 空闲连接存活时间(5分钟)
		hikariConfig.setIdleTimeout(300000);
		// 获取连接超时时间(10秒)
		hikariConfig.setConnectionTimeout(10000);
		// 核心保持的空闲连接数
		hikariConfig.setMinimumIdle(5);
		// 最大连接数
		hikariConfig.setMaximumPoolSize(30);
		// 每 60 秒发送一次 PING 帧保持连接活跃
		hikariConfig.setKeepaliveTime(60000);
		return new HikariDataSource(hikariConfig);
	}

}
