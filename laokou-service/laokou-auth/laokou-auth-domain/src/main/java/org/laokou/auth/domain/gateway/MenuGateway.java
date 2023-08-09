package org.laokou.auth.domain.gateway;

import java.util.List;

public interface MenuGateway {

	List<String> getPermissions(Long userId, Long tenantId, Integer superAdmin);

}
