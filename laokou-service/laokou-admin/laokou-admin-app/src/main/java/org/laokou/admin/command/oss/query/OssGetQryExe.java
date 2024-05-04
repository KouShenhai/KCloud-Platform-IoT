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

package org.laokou.admin.command.oss.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.OssConvertor;
import org.laokou.admin.dto.oss.OssGetQry;
import org.laokou.admin.dto.oss.clientobject.OssCO;
import org.laokou.admin.gatewayimpl.database.OssMapper;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DSConstant.TENANT;

/**
 * 查看OSS执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssGetQryExe {

	private final OssMapper ossMapper;

	private final OssConvertor ossConvertor;

	/**
	 * 执行查看OSS.
	 * @param qry 查看OSS参数
	 * @return OSS
	 */
	@DS(TENANT)
	public Result<OssCO> execute(OssGetQry qry) {
		return Result.ok(ossConvertor.convertClientObj(ossMapper.selectById(qry.getId())));
	}

}
