package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.DeptGateway;
import org.laokou.auth.domain.user.SuperAdmin;
import org.laokou.auth.gatewayimpl.database.DeptMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeptGatewayImpl implements DeptGateway {

	private final DeptMapper deptMapper;

	@Override
	public List<Long> getDeptIds(Long userId, Long tenantId, Integer superAdmin) {
		if (superAdmin == SuperAdmin.YES.ordinal()) {
			return deptMapper.getDeptIdsByTenantId(tenantId);
		}
		return deptMapper.getDeptIdsByUserIdAndTenantId(userId, tenantId);
	}

}
