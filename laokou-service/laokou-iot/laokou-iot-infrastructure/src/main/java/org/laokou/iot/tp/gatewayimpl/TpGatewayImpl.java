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

package org.laokou.iot.tp.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.tp.model.TpE;
import org.springframework.stereotype.Component;
import org.laokou.iot.tp.gateway.TpGateway;
import org.laokou.iot.tp.gatewayimpl.database.TpMapper;
import java.util.Arrays;
import org.laokou.iot.tp.convertor.TpConvertor;
import org.laokou.iot.tp.gatewayimpl.database.dataobject.TpDO;

/**
 *
 * 传输协议网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TpGatewayImpl implements TpGateway {

	private final TpMapper tpMapper;

	@Override
	public void create(TpE tpE) {
		tpMapper.insert(TpConvertor.toDataObject(tpE, true));
	}

	@Override
	public void update(TpE tpE) {
		TpDO tpDO = TpConvertor.toDataObject(tpE, false);
		tpDO.setVersion(tpMapper.selectVersion(tpE.getId()));
		tpMapper.updateById(tpDO);
	}

	@Override
	public void delete(Long[] ids) {
		tpMapper.deleteByIds(Arrays.asList(ids));
	}

}
