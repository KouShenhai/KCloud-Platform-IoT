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

package org.laokou.iot.product.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.product.model.ProductE;
import org.springframework.stereotype.Component;
import org.laokou.iot.product.gateway.ProductGateway;
import org.laokou.iot.product.gatewayimpl.database.ProductMapper;
import java.util.Arrays;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.iot.product.convertor.ProductConvertor;
import org.laokou.iot.product.gatewayimpl.database.dataobject.ProductDO;

/**
 *
 * 产品网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductGatewayImpl implements ProductGateway {

	private final ProductMapper productMapper;

	private final TransactionalUtil transactionalUtil;

	public void create(ProductE productE) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				productMapper.insert(ProductConvertor.toDataObject(productE, true));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("新增失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	public void update(ProductE productE) {
		ProductDO productDO = ProductConvertor.toDataObject(productE, false);
		productDO.setVersion(productMapper.selectVersion(productE.getId()));
		update(productDO);
	}

	public void delete(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				productMapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("删除失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	private void update(ProductDO productDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				productMapper.updateById(productDO);
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("修改失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

}
