/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.nacos.proxy;

import org.laokou.common.core.utils.RegexUtil;

import static org.laokou.common.i18n.common.NacosConstants.DEFAULT_PORT;
import static org.laokou.common.i18n.common.NetworkConstants.HTTP_PROTOCOL;
import static org.laokou.common.i18n.common.StringConstants.RISK;

/**
 * @author laokou
 */
public class HttpProtocolProxy extends AbstractProtocolProxy {

	@Override
	public String getTokenUri(String serverAddr) {
		if (internalIp(serverAddr)) {
			return HTTP_PROTOCOL.concat(serverAddr).concat(TOKEN_URI_SUFFIX);
		}
		return String.format("%s%s:%s", HTTP_PROTOCOL, serverAddr, DEFAULT_PORT).concat(TOKEN_URI_SUFFIX);
	}

	@Override
	public String getConfigUri(String serverAddr) {
		if (internalIp(serverAddr)) {
			return HTTP_PROTOCOL.concat(serverAddr).concat(CONFIG_URI_SUFFIX);
		}
		return String.format("%s%s:%s", HTTP_PROTOCOL, serverAddr, DEFAULT_PORT).concat(CONFIG_URI_SUFFIX);
	}

	@Override
	public boolean sslEnabled() {
		return false;
	}

	private boolean internalIp(String serverAddr) {
		int index = serverAddr.indexOf(RISK);
		if (index < 0) {
			return false;
		}
		return RegexUtil.ipRegex(serverAddr.substring(0, index));
	}

}
