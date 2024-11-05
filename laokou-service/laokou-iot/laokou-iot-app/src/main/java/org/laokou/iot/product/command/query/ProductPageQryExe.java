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

package org.laokou.iot.product.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.iot.product.dto.ProductPageQry;
import org.laokou.iot.product.dto.clientobject.ProductCO;
import org.laokou.iot.product.gatewayimpl.database.ProductMapper;
import org.laokou.iot.product.gatewayimpl.database.dataobject.ProductDO;
import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;
import org.laokou.iot.product.convertor.ProductConvertor;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 分页查询产品请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ProductPageQryExe {

	private final ProductMapper productMapper;

	public Result<Page<ProductCO>> execute(ProductPageQry qry) {
		try (ExecutorService executor = ThreadUtil.newVirtualTaskExecutor()) {
			CompletableFuture<List<ProductDO>> c1 = CompletableFuture
				.supplyAsync(() -> productMapper.selectObjectPage(qry.index()), executor);
			CompletableFuture<Long> c2 = CompletableFuture.supplyAsync(() -> productMapper.selectObjectCount(qry),
					executor);
			return Result
				.ok(Page.create(c1.get(30, TimeUnit.SECONDS).stream().map(ProductConvertor::toClientObject).toList(),
						c2.get(30, TimeUnit.SECONDS)));
		}
		catch (Exception e) {
			throw new SystemException("S_Product_PageQueryTimeout", "产品分页查询超时");
		}
	}

}
