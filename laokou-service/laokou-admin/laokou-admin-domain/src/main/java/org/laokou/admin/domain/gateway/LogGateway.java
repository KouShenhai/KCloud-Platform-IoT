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

package org.laokou.admin.domain.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.admin.domain.log.LoginLog;
import org.laokou.admin.domain.log.OperateLog;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;

/**
 * @author laokou
 */
@Schema(name = "LogGateway", description = "日志网关")
public interface LogGateway {

	/**
	 * 查询登录日志列表.
	 * @param loginLog 登录日志对象
	 * @param pageQuery 分页参数
	 * @return 登录日志列表
	 */
	Datas<LoginLog> loginList(LoginLog loginLog, PageQuery pageQuery);

	/**
	 * 查询操作日志列表.
	 * @param operateLog 操作日志对象
	 * @param pageQuery 分页参数
	 * @return 操作日志列表
	 */
	Datas<OperateLog> operateList(OperateLog operateLog, PageQuery pageQuery);

}
