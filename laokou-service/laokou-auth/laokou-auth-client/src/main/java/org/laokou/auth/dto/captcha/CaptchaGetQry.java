package org.laokou.auth.dto.captcha;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 * @author laokou
 */
@Data
@AllArgsConstructor
public class CaptchaGetQry extends CommonCommand {

	private String uuid;

}
