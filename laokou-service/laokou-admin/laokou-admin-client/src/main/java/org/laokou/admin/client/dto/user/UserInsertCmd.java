package org.laokou.admin.client.dto.user;

import lombok.Data;
import org.laokou.admin.client.dto.user.clientobject.UserCO;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 * @author laokou
 */
@Data
public class UserInsertCmd extends CommonCommand {

	private UserCO userCO;

}
