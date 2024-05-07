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

package org.laokou.admin.api;

import org.laokou.admin.dto.ip.IpRemoveCmd;
import org.laokou.admin.dto.ip.IpCreateCmd;
import org.laokou.admin.dto.ip.IpListQry;
import org.laokou.admin.dto.ip.IpRefreshCmd;
import org.laokou.admin.dto.ip.clientobject.IpCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

/**
 * IP管理.
 *
 * @author laokou
 */
public interface IpsServiceI {

	/**
	 * 新增IP.
	 * @param cmd 新增IP参数
	 */
	void create(IpCreateCmd cmd);

	/**
	 * 根据IDS删除IP.
	 * @param cmd 根据IDS删除IP参数
	 */
	void remove(IpRemoveCmd cmd);

	/**
	 * 查询IP列表.
	 * @param qry 查询IP列表参数
	 * @return IP列表
	 */
	Result<Datas<IpCO>> findList(IpListQry qry);

	/**
	 * 刷新IP至Redis.
	 * @param cmd 刷新IP至Redis参数
	 */
	void refresh(IpRefreshCmd cmd);

}
