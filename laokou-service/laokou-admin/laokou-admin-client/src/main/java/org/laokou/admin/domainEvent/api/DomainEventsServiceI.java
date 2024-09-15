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

package org.laokou.admin.domainEvent.api;

import org.laokou.admin.domainEvent.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.admin.domainEvent.dto.clientobject.DomainEventCO;

/**
 * 领域事件接口.
 *
 * @author laokou
 */
public interface DomainEventsServiceI {

	/**
	 * 保存领域事件.
	 * @param cmd 保存命令
	 */
	void save(DomainEventSaveCmd cmd);

	/**
	 * 修改领域事件.
	 * @param cmd 修改命令
	 */
	void modify(DomainEventModifyCmd cmd);

	/**
	 * 删除领域事件.
	 * @param cmd 删除命令
	 */
	void remove(DomainEventRemoveCmd cmd);

	/**
	 * 导入领域事件.
	 * @param cmd 导入命令
	 */
	void importI(DomainEventImportCmd cmd);

	/**
	 * 导出领域事件.
	 * @param cmd 导出命令
	 */
	void export(DomainEventExportCmd cmd);

	/**
	 * 分页查询领域事件.
	 * @param qry 分页查询请求
	 */
	Result<Page<DomainEventCO>> page(DomainEventPageQry qry);

	/**
	 * 查看领域事件.
	 * @param qry 查看请求
	 */
	Result<DomainEventCO> getById(DomainEventGetQry qry);

}
