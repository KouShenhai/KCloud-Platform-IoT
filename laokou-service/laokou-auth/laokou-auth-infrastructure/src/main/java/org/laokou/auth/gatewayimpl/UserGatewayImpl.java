package org.laokou.auth.gatewayimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.UserGateway;
import org.laokou.auth.domain.user.User;
import org.laokou.auth.gatewayimpl.database.UserMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.core.constant.Constant.SHARDING_SPHERE;

@Component
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

	private final UserMapper userMapper;

	@Override
	@DS(SHARDING_SPHERE)
	public User getUserByUsername(String username, Long tenantId, String type) {
		UserDO userDO = userMapper.getUserByUsernameAndTenantId(username, tenantId, type);
		if (userDO == null) {
			return null;
		}
		return ConvertUtil.sourceToTarget(userDO, User.class);
	}

}
