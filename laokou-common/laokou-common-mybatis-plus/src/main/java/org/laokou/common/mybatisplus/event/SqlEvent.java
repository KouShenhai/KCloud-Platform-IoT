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

package org.laokou.common.mybatisplus.event;

import org.laokou.common.i18n.dto.DomainEvent;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
public class SqlEvent extends DomainEvent {

	private String url;

	private String sql;

	private long costTime;

	private LocalDateTime nowDate;

	public SqlEvent(Object source) {
		super(source);
	}

	public SqlEvent(Object source, Clock clock) {
		super(source, clock);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public LocalDateTime getNowDate() {
		return nowDate;
	}

	public void setNowDate(LocalDateTime nowDate) {
		this.nowDate = nowDate;
	}

	@Override
	public String toString() {
		return "SqlEvent{" + "url='" + url + '\'' + ", sql='" + sql + '\'' + ", costTime=" + costTime + ", nowDate="
				+ nowDate + '}';
	}

}
