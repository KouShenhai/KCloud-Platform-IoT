/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.admin.domain.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.admin.domain.oss.Oss;
import org.laokou.admin.domain.oss.OssLog;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;

/**
 * @author laokou
 */
@Schema(name = "OssGateway", description = "OSS网关")
public interface OssGateway {

	/**
	 * 查询OSS列表.
	 * @param oss OSS对象
	 * @param pageQuery 分页参数
	 * @return OSS
	 */
	Datas<Oss> list(Oss oss, PageQuery pageQuery);

	/**
	 * 根据ID查看OSS.
	 * @param id ID
	 * @return OSS
	 */
	Oss getById(Long id);

	/**
	 * 新增OSS.
	 * @param oss OSS对象
	 * @return 新增结果
	 */
	Boolean insert(Oss oss);

	/**
	 * 修改OSS.
	 * @param oss OSS对象
	 * @return 修改结果
	 */
	Boolean update(Oss oss);

	/**
	 * 根据ID删除OSS.
	 * @param id ID
	 * @return 删除结果
	 */
	Boolean deleteById(Long id);

	/**
	 * 推送OSS日志.
	 * @param ossLog OSS日志对象
	 */
	void publish(OssLog ossLog);

}
