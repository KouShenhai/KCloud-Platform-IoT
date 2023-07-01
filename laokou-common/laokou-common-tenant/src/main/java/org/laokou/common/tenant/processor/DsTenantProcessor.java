/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.tenant.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.tenant.utils.DsUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.core.constant.Constant.DEFAULT_SOURCE;
import static org.laokou.common.core.constant.Constant.TENANT;

/**
 * @author laokou
 */
@Component
public class DsTenantProcessor extends DsProcessor {

	@Override
	public boolean matches(String key) {
		return key.startsWith(TENANT);
	}

	@Override
	public String doDetermineDatasource(MethodInvocation invocation, String key) {
		DsUtil dsUtil = SpringContextUtil.getBean(DsUtil.class);
		return dsUtil
				.loadDs(StringUtil.isNotEmpty(UserUtil.getSourceName()) ? UserUtil.getSourceName() : DEFAULT_SOURCE);
	}

}
