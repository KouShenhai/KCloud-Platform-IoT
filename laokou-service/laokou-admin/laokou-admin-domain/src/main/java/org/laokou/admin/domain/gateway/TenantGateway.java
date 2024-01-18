/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import jakarta.servlet.http.HttpServletResponse;
import org.laokou.admin.domain.tenant.Tenant;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;

/**
 * @author laokou
 */
@Schema(name = "TenantGateway", description = "租户网关")
public interface TenantGateway {

	/**
	 * 新增租户.
	 * @param tenant 租户对象
	 * @return 新增结果
	 */
	Boolean insert(Tenant tenant);

	/**
	 * 查询租户列表.
	 * @param tenant 租户对象
	 * @param pageQuery 分页参数
	 * @return 租户列表
	 */
	Datas<Tenant> list(Tenant tenant, PageQuery pageQuery);

	/**
	 * 根据ID查看租户.
	 * @param id ID
	 * @return 租户
	 */
	Tenant getById(Long id);

	/**
	 * 修改租户.
	 * @param tenant 租户对象
	 * @return 修改结果
	 */
	Boolean update(Tenant tenant);

	/**
	 * 根据ID删除租户.
	 * @param id ID
	 * @return 删除结果
	 */
	Boolean deleteById(Long id);

	/**
	 * 下载数据库压缩包.
	 * @param id ID
	 * @param response 响应对象
	 */
	void download(Long id, HttpServletResponse response);

}
