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

package org.laokou.iot.productCategory.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.iot.productCategory.dto.ProductCategoryModifyCmd;
import org.laokou.iot.productCategory.model.ProductCategoryE;
import org.springframework.stereotype.Component;
import org.laokou.iot.productCategory.convertor.ProductCategoryConvertor;
import org.laokou.iot.productCategory.ability.ProductCategoryDomainService;
import static org.laokou.common.tenant.constant.DSConstants.IOT;

/**
 *
 * 修改产品类别命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ProductCategoryModifyCmdExe {

	private final ProductCategoryDomainService productCategoryDomainService;

	private final TransactionalUtils transactionalUtils;

	@CommandLog
	public void executeVoid(ProductCategoryModifyCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push(IOT);
			ProductCategoryE productCategoryE = ProductCategoryConvertor.toEntity(cmd.getCo(), false);
			transactionalUtils
				.executeInTransaction(() -> productCategoryDomainService.updateProductCategory(productCategoryE));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
