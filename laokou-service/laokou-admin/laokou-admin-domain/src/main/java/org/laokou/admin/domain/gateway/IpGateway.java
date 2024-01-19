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
import org.laokou.admin.domain.ip.Ip;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;

/**
 * @author laokou
 */
@Schema(name = "IpGateway", description = "IP网关")
public interface IpGateway {

	/**
	 * 新增IP.
	 * @param ip IP对象
	 * @return 新增结果
	 */
	Boolean insert(Ip ip);

	/**
	 * 根据ID删除IP.
	 * @param id ID
	 * @return 删除结果
	 */
	Boolean deleteById(Long id);

	/**
	 * 查询IP列表.
	 * @param ip IP对象
	 * @param pageQuery 分页参数
	 * @return IP列表
	 */
	Datas<Ip> list(Ip ip, PageQuery pageQuery);

	/**
	 * 刷新IP至缓存.
	 * @param ip IP对象
	 * @return 刷新结果
	 */
	Boolean refresh(Ip ip);

}
