package org.laokou.auth.service;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.api.CaptchasServiceI;
import org.laokou.auth.command.captcha.query.CaptchaGetQryExe;
import org.laokou.auth.dto.captcha.CaptchaGetQry;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class CaptchasServiceImpl implements CaptchasServiceI {

	private final CaptchaGetQryExe captchaGetQryExe;

	@Override
	public Result<String> get(CaptchaGetQry qry) {
		return captchaGetQryExe.execute(qry);
	}

}
