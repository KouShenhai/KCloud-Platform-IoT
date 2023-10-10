package org.laokou.admin.command.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.UserConvertor;
import org.laokou.admin.domain.gateway.UserGateway;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.user.UserInsertCmd;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.mybatisplus.template.TableTemplate;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.admin.common.Constant.USER;
import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_USER;
import static org.laokou.common.mybatisplus.template.TableTemplate.MAX_TIME;
import static org.laokou.common.mybatisplus.template.TableTemplate.MIN_TIME;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserInsertCmdExe {

	private final UserGateway userGateway;

	private final UserMapper userMapper;

	@DS(USER)
	public Result<Boolean> execute(UserInsertCmd cmd) {
		UserCO co = cmd.getUserCO();
		List<String> dynamicTables = TableTemplate.getDynamicTables(MIN_TIME, MAX_TIME, BOOT_SYS_USER);
		Integer count = userMapper.getUserCount(dynamicTables, toUserDO(co));
		if (count > 0) {
			throw new GlobalException("用户名已存在，请重新输入");
		}
		return Result.of(userGateway.insert(toUser(co)));
	}

	private UserDO toUserDO(UserCO co) {
		UserDO userDO = ConvertUtil.sourceToTarget(co, UserDO.class);
		userDO.setTenantId(UserUtil.getTenantId());
		userDO.setUsername(AesUtil.encrypt(userDO.getUsername()));
		return userDO;
	}

	private User toUser(UserCO co) {
		User user = UserConvertor.toEntity(co);
		user.setTenantId(UserUtil.getTenantId());
		user.setCreator(UserUtil.getUserId());
		user.setDeptId(UserUtil.getDeptId());
		user.setDeptPath(UserUtil.getDeptPath());
		return user;
	}

}
