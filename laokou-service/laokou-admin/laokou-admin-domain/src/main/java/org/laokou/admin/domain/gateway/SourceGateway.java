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
import org.laokou.admin.domain.source.Source;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;

/**
 * @author laokou
 */
@Schema(name = "SourceGateway", description = "数据源网关")
public interface SourceGateway {

	/**
	 * 查询数据源列表.
	 * @param source 数据源对象
	 * @param pageQuery 分页参数
	 * @return 数据源列表
	 */
	Datas<Source> list(Source source, PageQuery pageQuery);

	/**
	 * 根据ID查看数据源.
	 * @param id ID
	 * @return 数据源
	 */
	Source getById(Long id);

	/**
	 * 新增数据源.
	 * @param source 数据源对象
	 * @return 新增结果
	 */
	Boolean insert(Source source);

	/**
	 * 修改数据源.
	 * @param source 数据源对象
	 * @return 修改结果
	 */
	Boolean update(Source source);

	/**
	 * 根据ID删除数据源.
	 * @param id ID
	 * @return 删除结果
	 */
	Boolean deleteById(Long id);

}
