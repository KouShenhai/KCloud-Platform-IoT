/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.ossLog.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.ossLog.convertor.OssLogConvertor;
import org.laokou.admin.ossLog.gateway.OssLogGateway;
import org.laokou.admin.ossLog.gatewayimpl.database.OssLogMapper;
import org.laokou.admin.ossLog.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.admin.ossLog.model.OssLogE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * OSS日志网关实现.
 *
 * @author laokou
 */
@Component("adminOssLogGateway")
@RequiredArgsConstructor
public class OssLogGatewayImpl implements OssLogGateway {

	private final OssLogMapper adminOssLogMapper;

	@Override
	public void createOssLog(OssLogE ossLogE) {
		adminOssLogMapper.insert(OssLogConvertor.toDataObject(ossLogE));
	}

	@Override
	public void updateOssLog(OssLogE ossLogE) {
		OssLogDO ossLogDO = OssLogConvertor.toDataObject(ossLogE);
		ossLogDO.setVersion(adminOssLogMapper.selectVersion(ossLogE.getId()));
		adminOssLogMapper.updateById(ossLogDO);
	}

	@Override
	public void deleteOssLog(Long[] ids) {
		adminOssLogMapper.deleteByIds(Arrays.asList(ids));
	}

}
