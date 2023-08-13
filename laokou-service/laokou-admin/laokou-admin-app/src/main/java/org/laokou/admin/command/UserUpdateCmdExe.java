package org.laokou.admin.command;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.dto.UserUpdateCmd;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserUpdateCmdExe {

	private final UserGateway userGateway;

	public Result<Boolean> execute(UserUpdateCmd cmd) {
		User user = ConvertUtil.sourceToTarget(cmd.getUserCO(), User.class);
		return Result.of(userGateway.update(user));
	}

}
