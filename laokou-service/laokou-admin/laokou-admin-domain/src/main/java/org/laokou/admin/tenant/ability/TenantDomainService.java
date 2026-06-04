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

package org.laokou.admin.tenant.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.tenant.gateway.TenantGateway;
import org.laokou.admin.tenant.model.TenantE;
import org.laokou.common.core.util.ArrayUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 租户领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantDomainService {

	private final TenantGateway adminTenantGateway;

	public void createTenant(TenantE tenantE) {
		checkTenantParam(tenantE, false);
		adminTenantGateway.createTenant(tenantE);
	}

	public void updateTenant(TenantE tenantE) {
		checkTenantParam(tenantE, true);
		adminTenantGateway.updateTenant(tenantE);
	}

	public void deleteTenant(Long[] ids) {
		checkRemoveParam(ids);
		adminTenantGateway.deleteTenant(ids);
	}

	private void checkTenantParam(TenantE tenantE, boolean modify) {
		ParamValidator.validate("Tenant", validateCo(tenantE), validateId(tenantE, modify), validateName(tenantE),
				validateCode(tenantE), validateSourceId(tenantE), validatePackageId(tenantE),
				validateUniqueCode(tenantE));
	}

	private void checkRemoveParam(Long[] ids) {
		ParamValidator.validate("Tenant", validateIds(ids), validateDefaultTenant(ids));
	}

	private ParamValidator.Validate validateCo(TenantE tenantE) {
		if (ObjectUtils.isNull(tenantE)) {
			return ParamValidator.invalidate("租户不能为空");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateId(TenantE tenantE, boolean modify) {
		if (ObjectUtils.isNull(tenantE)) {
			return ParamValidator.validate();
		}
		if (modify && ObjectUtils.isNull(tenantE.getId())) {
			return ParamValidator.invalidate("租户ID不能为空");
		}
		if (modify && tenantE.getId() < 1) {
			return ParamValidator.invalidate("租户ID错误");
		}
		if (modify && !adminTenantGateway.existsTenant(tenantE.getId())) {
			return ParamValidator.invalidate("租户不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateName(TenantE tenantE) {
		if (ObjectUtils.isNull(tenantE)) {
			return ParamValidator.validate();
		}
		String name = tenantE.getName();
		if (StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("租户名称不能为空");
		}
		if (name.length() > 100) {
			return ParamValidator.invalidate("租户名称不能超过100个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateCode(TenantE tenantE) {
		if (ObjectUtils.isNull(tenantE)) {
			return ParamValidator.validate();
		}
		String code = tenantE.getCode();
		if (StringExtUtils.isEmpty(code)) {
			return ParamValidator.invalidate("租户编码不能为空");
		}
		if (code.length() > 30) {
			return ParamValidator.invalidate("租户编码不能超过30个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateSourceId(TenantE tenantE) {
		if (ObjectUtils.isNull(tenantE)) {
			return ParamValidator.validate();
		}
		Long sourceId = tenantE.getSourceId();
		if (ObjectUtils.isNull(sourceId)) {
			return ParamValidator.invalidate("数据源ID不能为空");
		}
		if (sourceId < 0) {
			return ParamValidator.invalidate("数据源ID不能小于0");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validatePackageId(TenantE tenantE) {
		if (ObjectUtils.isNull(tenantE)) {
			return ParamValidator.validate();
		}
		Long packageId = tenantE.getPackageId();
		if (ObjectUtils.isNull(packageId)) {
			return ParamValidator.invalidate("套餐ID不能为空");
		}
		if (packageId < 0) {
			return ParamValidator.invalidate("套餐ID不能小于0");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateUniqueCode(TenantE tenantE) {
		if (ObjectUtils.isNull(tenantE) || StringExtUtils.isEmpty(tenantE.getCode())) {
			return ParamValidator.validate();
		}
		if (adminTenantGateway.existsCode(tenantE.getId(), tenantE.getCode())) {
			return ParamValidator.invalidate("租户编码已存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateIds(Long[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return ParamValidator.invalidate("租户IDS不能为空");
		}
		if (Arrays.stream(ids).anyMatch(id -> ObjectUtils.isNull(id) || id < 1)) {
			return ParamValidator.invalidate("租户IDS错误");
		}
		if (!adminTenantGateway.existsTenant(ids)) {
			return ParamValidator.invalidate("租户不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateDefaultTenant(Long[] ids) {
		if (ObjectUtils.isNotNull(ids) && ids.length > 0 && adminTenantGateway.existsDefaultTenant(ids)) {
			return ParamValidator.invalidate("默认租户不允许删除");
		}
		return ParamValidator.validate();
	}

}
