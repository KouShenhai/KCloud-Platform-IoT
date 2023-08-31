package org.laokou.auth.api;

import org.laokou.auth.dto.secret.SecretGetQry;
import org.laokou.common.i18n.dto.Result;

/**
 * @author laokou
 */
public interface SecretsServiceI {

	Result<String> get(SecretGetQry qry);

}
