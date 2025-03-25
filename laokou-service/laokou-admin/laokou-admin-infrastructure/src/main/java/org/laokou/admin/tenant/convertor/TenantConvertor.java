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

package org.laokou.admin.tenant.convertor;

import org.laokou.admin.tenant.dto.clientobject.TenantCO;
import org.laokou.admin.tenant.model.TenantE;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.core.util.IdGenerator;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.tenant.mapper.TenantDO;

/**
 * 租户转换器.
 *
 * @author laokou
 */
public class TenantConvertor {

	public static TenantDO toDataObject(TenantE tenantE) {
		TenantDO tenantDO = ConvertUtils.sourceToTarget(tenantE, TenantDO.class);
		if (ObjectUtils.isNull(tenantDO.getId())) {
			tenantDO.setId(IdGenerator.defaultSnowflakeId());
		}
		return tenantDO;
	}

	public static TenantCO toClientObject(TenantDO tenantDO) {
		return ConvertUtils.sourceToTarget(tenantDO, TenantCO.class);
	}

	public static TenantE toEntity(TenantCO tenantCO) {
		return ConvertUtils.sourceToTarget(tenantCO, TenantE.class);
	}

}
