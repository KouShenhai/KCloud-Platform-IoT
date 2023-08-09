package org.laokou.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.laokou.common.i18n.dto.CommonCommand;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CaptchaGetQry extends CommonCommand {

	private String uuid;

}
