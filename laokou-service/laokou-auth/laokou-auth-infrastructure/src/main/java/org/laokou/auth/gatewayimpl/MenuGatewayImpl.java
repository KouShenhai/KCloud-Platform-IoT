package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.MenuGateway;
import org.laokou.auth.domain.user.SuperAdmin;
import org.laokou.auth.gatewayimpl.database.MenuMapper;
import org.laokou.auth.gatewayimpl.database.TenantMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.auth.common.Constant.*;

@Component
@RequiredArgsConstructor
public class MenuGatewayImpl implements MenuGateway {

    private final MenuMapper menuMapper;
    private final TenantMapper tenantMapper;

    @Override
    public List<String> getPermissions(Long userId, Long tenantId, Integer superAdmin) {
        if (superAdmin == SuperAdmin.YES.ordinal()) {
            if (tenantId == DEFAULT_TENANT) {
                return menuMapper.getPermissions();
            }
            return tenantMapper.getPermissionsByTenantId(tenantId);
        }
        return menuMapper.getPermissionsByUserId(userId);
    }
}
