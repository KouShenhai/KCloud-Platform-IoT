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

package org.laokou.iot.gateway.gatewayimpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.iot.gateway.model.GatewayE;
import org.springframework.stereotype.Component;
import org.laokou.iot.gateway.gateway.GatewayGateway;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayMapper;
import java.util.Arrays;
import org.laokou.iot.gateway.convertor.GatewayConvertor;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.laokou.iot.product.gatewayimpl.database.ProductMapper;
import org.laokou.iot.product.gatewayimpl.database.dataobject.ProductDO;

/**
 *
 * 网关网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class GatewayGatewayImpl implements GatewayGateway {

	private final GatewayMapper gatewayMapper;

	private final ProductMapper productMapper;

	@Override
	public void createGateway(GatewayE gatewayE) {
		gatewayMapper.insert(GatewayConvertor.toDataObject(1L, gatewayE, true));
	}

	@Override
	public void updateGateway(GatewayE gatewayE) {
		GatewayDO gatewayDO = GatewayConvertor.toDataObject(null, gatewayE, false);
		gatewayDO.setVersion(gatewayMapper.selectVersion(gatewayE.getId()));
		gatewayMapper.updateById(gatewayDO);
	}

	@Override
	public void deleteGateway(Long[] ids) {
		gatewayMapper.deleteByIds(Arrays.asList(ids));
	}

	@Override
	public boolean existsGatewayKey(Long id, String gatewayKey) {
		return gatewayMapper.selectCount(Wrappers.lambdaQuery(GatewayDO.class)
			.eq(GatewayDO::getGatewayKey, gatewayKey)
			.ne(ObjectUtils.isNotNull(id), GatewayDO::getId, id)) > 0;
	}

	@Override
	public boolean existsGateway(Long id) {
		return gatewayMapper.selectCount(Wrappers.lambdaQuery(GatewayDO.class).eq(GatewayDO::getId, id)) > 0;
	}

	@Override
	public boolean existsGateway(Long[] ids) {
		return gatewayMapper
			.selectCount(Wrappers.lambdaQuery(GatewayDO.class).in(GatewayDO::getId, Arrays.asList(ids))) == ids.length;
	}

	@Override
	public boolean existsProduct(Long productId) {
		return productMapper.selectCount(Wrappers.lambdaQuery(ProductDO.class).eq(ProductDO::getId, productId)) > 0;
	}

	@Override
	public String findGatewayKeyById(Long id) {
		GatewayDO gatewayDO = gatewayMapper.selectById(id);
		return ObjectUtils.isNull(gatewayDO) ? null : gatewayDO.getGatewayKey();
	}

}
