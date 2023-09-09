package org.laokou.admin.command.user;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.user.UserInsertCmd;
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserInsertCmdExe {

	private final UserGateway userGateway;

	public Result<Boolean> execute(UserInsertCmd cmd) {
		return Result.of(userGateway.insert(UserConvertor.toEntity(cmd.getUserCO())));
	}

}
