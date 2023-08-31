package org.laokou.auth.service;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.api.SecretsServiceI;
import org.laokou.auth.command.secret.query.SecretGetQryExe;
import org.laokou.auth.dto.secret.SecretGetQry;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SecretsServiceImpl implements SecretsServiceI {

	private final SecretGetQryExe secretGetQryExe;

	@Override
	public Result<String> get(SecretGetQry qry) {
		return secretGetQryExe.execute(qry);
	}

}
