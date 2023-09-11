package org.laokou.admin.command.user;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.dto.user.UserInsertCmd;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.jasypt.utils.AesUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserInsertCmdExe {

	private final UserGateway userGateway;
	private final UserMapper userMapper;

	public Result<Boolean> execute(UserInsertCmd cmd) {
		UserCO userCO = cmd.getUserCO();
		Long count = userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, AesUtil.encrypt(userCO.getUsername())));
		if (count > 0) {
			throw new GlobalException("用户名已存在，请重新输入");
		}
		return Result.of(userGateway.insert(UserConvertor.toEntity(userCO)));
	}

}
