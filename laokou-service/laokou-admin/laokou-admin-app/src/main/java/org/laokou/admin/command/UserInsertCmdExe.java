package org.laokou.admin.command;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.dto.UserInsertCmd;
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
public class UserInsertCmdExe {

    private final UserGateway userGateway;

    public Result<Boolean> execute(UserInsertCmd cmd) {
        User user = ConvertUtil.sourceToTarget(cmd.getUserCO(), User.class);
        return Result.of(userGateway.insert(user));
    }

}
