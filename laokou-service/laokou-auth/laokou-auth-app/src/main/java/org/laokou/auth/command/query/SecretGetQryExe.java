/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.auth.command.query;

import org.laokou.auth.dto.clientobject.SecretCO;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.crypto.utils.RSAUtil;
import org.springframework.stereotype.Component;

/**
 * 获取密钥执行器.
 *
 * @author laokou
 */
@Component
public class SecretGetQryExe {

	/**
	 * 执行获取密钥.
	 * @return 密钥
	 */
	public Result<SecretCO> execute() {
		return Result.ok(new SecretCO(RSAUtil.getPublicKey()));
	}

}
