package org.laokou.auth.api;

import org.laokou.auth.dto.CaptchaGetQry;
import org.laokou.common.i18n.dto.Result;

public interface CaptchasServiceI {

	Result<String> get(CaptchaGetQry qry);

}
