package org.laokou.admin.command.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.user.UserUpdateCmd;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.Constant.USER;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserUpdateCmdExe {

	private final UserGateway userGateway;

	private final UserMapper userMapper;

	@DS(USER)
	public Result<Boolean> execute(UserUpdateCmd cmd) {
		UserCO userCO = cmd.getUserCO();
		Long count = userMapper.selectCount(Wrappers.lambdaQuery(UserDO.class)
				.eq(UserDO::getUsername, AesUtil.encrypt(userCO.getUsername())).ne(UserDO::getId, userCO.getId()));
		if (count > 0) {
			throw new GlobalException("用户名已存在，请重新输入");
		}
		return Result.of(userGateway.update(toUser(userCO)));
	}

	private User toUser(UserCO userCO) {
		User user = UserConvertor.toEntity(userCO);
		user.setTenantId(UserUtil.getTenantId());
		user.setCreator(UserUtil.getUserId());
		user.setEditor(UserUtil.getUserId());
		user.setDeptId(UserUtil.getDeptId());
		user.setDeptPath(UserUtil.getDeptPath());
		return user;
	}

}
