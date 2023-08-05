package org.laokou.auth.domain.gateway;

import org.laokou.auth.domain.user.User;

public interface UserGateway {

    /**
     * 查询用户
     * @param username 用户名
     * @param tenantId 租户ID
     * @param type 类型（password、mail、mobile）
     * @return User
     */
    User getUserByUsername(String username,Long tenantId,String type);

}
