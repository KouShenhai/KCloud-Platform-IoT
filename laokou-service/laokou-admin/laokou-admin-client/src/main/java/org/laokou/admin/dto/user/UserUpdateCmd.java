package org.laokou.admin.dto.user;

import lombok.Data;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 * @author laokou
 */
@Data
public class UserUpdateCmd extends CommonCommand {

	private UserCO userCO;

}
