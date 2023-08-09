package org.laokou.auth.command.query;

import org.laokou.common.i18n.dto.Result;
import org.laokou.common.jasypt.utils.RsaUtil;
import org.springframework.stereotype.Component;

@Component
public class SecretGetQryExe {

	public Result<String> execute() {
		return Result.of(RsaUtil.getPublicKey());
	}

}
