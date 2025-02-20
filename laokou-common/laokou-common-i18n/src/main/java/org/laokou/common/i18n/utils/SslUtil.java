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

package org.laokou.common.i18n.utils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author laokou
 */
public final class SslUtil {

	/**
	 * TLS协议版本.
	 */
	private static final String TLS_PROTOCOL_VERSION = "TLSv1.3";

	private SslUtil() {
	}

	/**
	 * ssl上下文.
	 * @return ssl上下文
	 */
	public static SSLContext sslContext() throws NoSuchAlgorithmException, KeyManagementException {
		// X.509是密码学里公钥证书的格式标准，作为证书标准
		X509TrustManager disabledTrustManager = new DisableValidationTrustManager();
		// 信任库
		TrustManager[] trustManagers = new TrustManager[] { disabledTrustManager };
		// 怎么选择加密协议，请看 ProtocolVersion
		// 为什么能找到对应的加密协议 请查看 SSLContextSpi
		SSLContext sslContext = SSLContext.getInstance(TLS_PROTOCOL_VERSION);
		sslContext.init(null, trustManagers, null);
		return sslContext;
	}

	public static void ignoreSSLTrust() throws NoSuchAlgorithmException, KeyManagementException {
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext().getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
	}

	public static class DisableValidationTrustManager implements X509TrustManager {

		public static final X509TrustManager INSTANCE = new DisableValidationTrustManager();

		public DisableValidationTrustManager() {
		}

		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
		}

		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

	}

}
