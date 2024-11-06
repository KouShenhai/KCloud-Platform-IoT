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

package org.laokou.admin.oss.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.oss.model.OssE;
import org.springframework.stereotype.Component;
import org.laokou.admin.oss.gateway.OssGateway;
import org.laokou.admin.oss.gatewayimpl.database.OssMapper;

import java.util.Arrays;

import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.admin.oss.convertor.OssConvertor;
import org.laokou.admin.oss.gatewayimpl.database.dataobject.OssDO;

/**
 * OSS网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssGatewayImpl implements OssGateway {

	private final OssMapper ossMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public void create(OssE ossE) {
		transactionalUtil.executeInTransaction(() -> ossMapper.insert(OssConvertor.toDataObject(ossE)));
	}

	@Override
	public void update(OssE ossE) {
		OssDO ossDO = OssConvertor.toDataObject(ossE);
		ossDO.setVersion(ossMapper.selectVersion(ossE.getId()));
		update(ossDO);
	}

	@Override
	public void delete(Long[] ids) {
		transactionalUtil.executeInTransaction(() -> ossMapper.deleteByIds(Arrays.asList(ids)));
	}

	private void update(OssDO ossDO) {
		transactionalUtil.executeInTransaction(() -> ossMapper.updateById(ossDO));
	}

}
