/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.oss.gatewayimpl;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.common.data.cache.annotation.DistributedCache;
import org.laokou.common.data.cache.constant.NameConstants;
import org.laokou.common.data.cache.model.OperateTypeEnum;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.oss.convertor.OssConvertor;
import org.laokou.oss.convertor.OssLogConvertor;
import org.laokou.oss.gateway.OssLogGateway;
import org.laokou.oss.gatewayimpl.database.OssLogMapper;
import org.laokou.oss.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.oss.model.OssLogE;
import org.laokou.oss.model.OssUploadV;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssLogGatewayImpl implements OssLogGateway {

	private final OssLogMapper ossLogMapper;

	@Override
	@DistributedCache(name = NameConstants.OSS_LOG, key = "#md5", operateType = OperateTypeEnum.GET)
	public OssUploadV getOssInfoByMd5(String md5) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.DOMAIN);
			OssLogDO ossLogDO = ossLogMapper.selectOne(Wrappers.lambdaQuery(OssLogDO.class)
				.select(OssLogDO::getId, OssLogDO::getUrl)
				.eq(OssLogDO::getMd5, md5));
			return ObjectUtils.isNotNull(ossLogDO) ? OssConvertor.toValueObject(ossLogDO) : null;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	@Override
	public void createOssLog(OssLogE ossLogE) {
		ossLogMapper.insert(OssLogConvertor.toDataObject(ossLogE));
	}

}
