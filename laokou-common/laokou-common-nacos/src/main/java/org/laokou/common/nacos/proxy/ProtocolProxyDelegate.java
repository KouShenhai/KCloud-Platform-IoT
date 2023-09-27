/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

/**
 * @author laokou
 */
public class ProtocolProxyDelegate implements ProtocolProxy {

	private final boolean sslEnabled;

	private final HttpProtocolProxy httpProtocolProxy;

	private final HttpsProtocolProxy httpsProtocolProxy;

	public ProtocolProxyDelegate(boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
		this.httpProtocolProxy = new HttpProtocolProxy();
		this.httpsProtocolProxy = new HttpsProtocolProxy();
	}

	@Override
	public String getTokenUri(String serverAddr) {
		return getProxy().getTokenUri(serverAddr);
	}

	@Override
	public String getConfigUri(String serverAddr) {
		return getProxy().getConfigUri(serverAddr);
	}

	@Override
	public boolean sslEnabled() {
		return getProxy().sslEnabled();
	}

	private ProtocolProxy getProxy() {
		return httpProtocolProxy;
	}

}
