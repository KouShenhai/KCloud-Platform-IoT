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

package org.laokou.common.mybatisplus.handler.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

/**
 * @author laokou
 */
@Getter
@Setter
@ToString
public class SqlLogEvent extends ApplicationEvent {

	private String appName;

	private String sql;

	private long costTime;

	private Instant createTime;

	public SqlLogEvent(Object source, String appName, String sql, long costTime, Instant createTime) {
		super(source);
		this.sql = sql;
		this.appName = appName;
		this.costTime = costTime;
		this.createTime = createTime;
	}

}
