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

package org.laokou.admin.ip.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.ip.convertor.IpConvertor;
import org.laokou.admin.ip.gateway.IpGateway;
import org.laokou.admin.ip.gatewayimpl.database.IpMapper;
import org.laokou.admin.ip.gatewayimpl.database.dataobject.IpDO;
import org.laokou.admin.ip.model.IpE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * IP网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class IpGatewayImpl implements IpGateway {

	private final IpMapper ipMapper;

	@Override
	public void create(IpE ipE) {
		ipMapper.insert(IpConvertor.toDataObject(ipE));
	}

	@Override
	public void update(IpE ipE) {
		IpDO ipDO = IpConvertor.toDataObject(ipE);
		ipDO.setVersion(ipMapper.selectVersion(ipE.getId()));
		ipMapper.updateById(ipDO);
	}

	@Override
	public void delete(Long[] ids) {
		ipMapper.deleteByIds(Arrays.asList(ids));
	}

}
