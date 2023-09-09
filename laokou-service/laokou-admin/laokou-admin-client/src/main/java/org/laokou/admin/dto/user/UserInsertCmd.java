package org.laokou.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.admin.dto.user.clientobject.UserCO;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 * @author laokou
 */
@Data
public class UserInsertCmd extends CommonCommand {

	@Schema
	private UserCO userCO;

}
