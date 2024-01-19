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

package org.laokou.admin.command.oss.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.OssConvertor;
import org.laokou.admin.domain.gateway.OssGateway;
import org.laokou.admin.domain.oss.Oss;
import org.laokou.admin.dto.oss.OssListQry;
import org.laokou.admin.dto.oss.clientobject.OssCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 查询OSS列表执行器.
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssListQryExe {

	private final OssGateway ossGateway;

	private final OssConvertor ossConvertor;

	/**
	 * 执行查询OSS列表
	 * @param qry 查询OSS列表参数
	 * @return OSS列表
	 */
	@DS(TENANT)
	public Result<Datas<OssCO>> execute(OssListQry qry) {
		Oss oss = ConvertUtil.sourceToTarget(qry, Oss.class);
		Datas<Oss> newPage = ossGateway.list(oss, qry);
		Datas<OssCO> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(ossConvertor.convertClientObjectList(newPage.getRecords()));
		return Result.of(datas);
	}

}
