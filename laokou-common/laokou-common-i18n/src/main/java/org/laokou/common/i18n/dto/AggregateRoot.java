/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.dto;

import lombok.Getter;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_TENANT_ID_REQUIRE;
import static org.laokou.common.i18n.common.exception.ParamException.SYSTEM_ID_REQUIRE;

/**
 * 聚合根.
 *
 * @author laokou
 */
@Getter
public abstract class AggregateRoot<ID> extends Identifier<ID> {

	/**
	 * 创建人.
	 */
	protected ID creator;

	/**
	 * 编辑人.
	 */
	protected ID editor;

	/**
	 * 部门ID.
	 */
	protected ID deptId;

	/**
	 * 部门PATH.
	 */
	protected String deptPath;

	/**
	 * 租户ID.
	 */
	protected ID tenantId;

	/**
	 * 创建时间.
	 */
	protected LocalDateTime createDate;

	/**
	 * 修改时间.
	 */
	protected LocalDateTime updateDate;

	/**
	 * 数据源名称.
	 */
	protected String sourceName;

	/**
	 * 应用名称.
	 */
	protected String appName;

	/**
	 * 事件集合.
	 */
	private List<DomainEvent<ID>> events;

	public void checkNullId() {
		if (ObjectUtil.isNull(this.id)) {
			throw new SystemException(SYSTEM_ID_REQUIRE, ValidatorUtil.getMessage(SYSTEM_ID_REQUIRE));
		}
	}

	protected void checkNullTenantId() {
		if (ObjectUtil.isNull(this.tenantId)) {
			throw new AuthException(OAUTH2_TENANT_ID_REQUIRE, ValidatorUtil.getMessage(OAUTH2_TENANT_ID_REQUIRE));
		}
	}

	protected void addEvent(DomainEvent<ID> event) {
		events().add(event);
	}

	public void clearEvents() {
		events = null;
	}

	private List<DomainEvent<ID>> events() {
		if (ObjectUtil.isNull(events)) {
			events = new ArrayList<>(16);
		}
		return events;
	}

}
