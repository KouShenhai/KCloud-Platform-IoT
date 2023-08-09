package org.laokou.auth.domain.gateway;

import java.util.List;

public interface DeptGateway {

	List<Long> getDeptIds(Long userId, Long tenantId, Integer superAdmin);

}
