/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.crypto.utils.RsaUtil;

import static org.laokou.common.i18n.common.SysConstant.DEFAULT_PASSWORD;
import static org.laokou.common.i18n.common.SysConstant.DEFAULT_USERNAME;

/**
 * @author laokou
 */
@Slf4j
public class RsaTest {

	public static void main(String[] args) {
		String publicKey = RsaUtil.getPublicKey();
		String privateKey = RsaUtil.getPrivateKey();
		String username = RsaUtil.encryptByPublicKey(DEFAULT_USERNAME, publicKey);
		String pwd = RsaUtil.encryptByPublicKey(DEFAULT_PASSWORD, publicKey);
		log.info(username);
		log.info(pwd);
		log.info(RsaUtil.decryptByPrivateKey(username, privateKey));
		log.info(RsaUtil.decryptByPrivateKey(pwd, privateKey));
	}

}
