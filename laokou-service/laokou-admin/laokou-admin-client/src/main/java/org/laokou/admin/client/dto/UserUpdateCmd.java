package org.laokou.admin.client.dto;

import lombok.Data;
import org.laokou.admin.client.dto.clientobject.UserCO;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 * @author laokou
 */
@Data
public class UserUpdateCmd extends CommonCommand {

	private UserCO userCO;

}
