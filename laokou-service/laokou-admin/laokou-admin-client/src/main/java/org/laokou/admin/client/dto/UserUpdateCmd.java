package org.laokou.admin.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.laokou.admin.client.dto.clientobject.UserCO;
import org.laokou.common.i18n.dto.CommonCommand;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserUpdateCmd extends CommonCommand {

	private UserCO userCO;

}
