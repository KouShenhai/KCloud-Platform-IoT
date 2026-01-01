/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.SslUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author laokou
 */
class SslUtilsTest {

	@Test
	void test_sslContext_notNull() throws NoSuchAlgorithmException, KeyManagementException {
		// Test that sslContext returns a non-null SSLContext
		SSLContext sslContext = SslUtils.sslContext();
		Assertions.assertThat(sslContext).isNotNull();
	}

	@Test
	void test_sslContext_protocol() throws NoSuchAlgorithmException, KeyManagementException {
		// Test that sslContext uses TLSv1.3 protocol
		SSLContext sslContext = SslUtils.sslContext();
		Assertions.assertThat(sslContext).isNotNull();
		Assertions.assertThat(sslContext.getProtocol()).isEqualTo("TLSv1.3");
	}

	@Test
	void test_sslContext_socketFactory() throws NoSuchAlgorithmException, KeyManagementException {
		// Test that sslContext can create a socket factory
		SSLContext sslContext = SslUtils.sslContext();
		SSLSocketFactory socketFactory = sslContext.getSocketFactory();
		Assertions.assertThat(socketFactory).isNotNull();
	}

	@Test
	void test_sslContext_multipleCalls() throws NoSuchAlgorithmException, KeyManagementException {
		// Test that multiple calls to sslContext return different instances
		SSLContext sslContext1 = SslUtils.sslContext();
		SSLContext sslContext2 = SslUtils.sslContext();
		Assertions.assertThat(sslContext1).isNotNull();
		Assertions.assertThat(sslContext2).isNotNull();
		// Each call should create a new instance
		Assertions.assertThat(sslContext1).isNotSameAs(sslContext2);
	}

	@Test
	void test_ignoreSSLTrust_doesNotThrow() {
		// Test that ignoreSSLTrust executes without throwing exceptions
		Assertions.assertThatCode(SslUtils::ignoreSSLTrust).doesNotThrowAnyException();
	}

	@Test
	void test_ignoreSSLTrust_setsDefaultSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
		// Test that ignoreSSLTrust sets the default SSL socket factory
		SSLSocketFactory originalFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
		SslUtils.ignoreSSLTrust();
		SSLSocketFactory newFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
		Assertions.assertThat(newFactory).isNotNull();
		// The factory should have been changed
		Assertions.assertThat(newFactory).isNotSameAs(originalFactory);
	}

	@Test
	void test_ignoreSSLTrust_setsHostnameVerifier() throws NoSuchAlgorithmException, KeyManagementException {
		// Test that ignoreSSLTrust sets the hostname verifier
		SslUtils.ignoreSSLTrust();
		Assertions.assertThat(HttpsURLConnection.getDefaultHostnameVerifier()).isNotNull();
		// Verify that the hostname verifier always returns true
		boolean result = HttpsURLConnection.getDefaultHostnameVerifier().verify("example.com", null);
		Assertions.assertThat(result).isTrue();
	}

	@Test
	void test_disableValidationTrustManager_instance() {
		// Test that DisableValidationTrustManager.INSTANCE is not null
		X509TrustManager trustManager = SslUtils.DisableValidationTrustManager.INSTANCE;
		Assertions.assertThat(trustManager).isNotNull();
	}

	@Test
	void test_disableValidationTrustManager_singleton() {
		// Test that INSTANCE is a singleton
		X509TrustManager instance1 = SslUtils.DisableValidationTrustManager.INSTANCE;
		X509TrustManager instance2 = SslUtils.DisableValidationTrustManager.INSTANCE;
		Assertions.assertThat(instance1).isSameAs(instance2);
	}

	@Test
	void test_disableValidationTrustManager_checkClientTrusted() {
		// Test that checkClientTrusted does not throw exceptions
		X509TrustManager trustManager = SslUtils.DisableValidationTrustManager.INSTANCE;
		X509Certificate[] certificates = new X509Certificate[0];
		Assertions.assertThatCode(() -> trustManager.checkClientTrusted(certificates, "RSA"))
			.doesNotThrowAnyException();
	}

	@Test
	void test_disableValidationTrustManager_checkServerTrusted() {
		// Test that checkServerTrusted does not throw exceptions
		X509TrustManager trustManager = SslUtils.DisableValidationTrustManager.INSTANCE;
		X509Certificate[] certificates = new X509Certificate[0];
		Assertions.assertThatCode(() -> trustManager.checkServerTrusted(certificates, "RSA"))
			.doesNotThrowAnyException();
	}

	@Test
	void test_disableValidationTrustManager_checkClientTrustedWithNull() {
		// Test that checkClientTrusted handles null certificates
		X509TrustManager trustManager = SslUtils.DisableValidationTrustManager.INSTANCE;
		Assertions.assertThatCode(() -> trustManager.checkClientTrusted(null, "RSA")).doesNotThrowAnyException();
	}

	@Test
	void test_disableValidationTrustManager_checkServerTrustedWithNull() {
		// Test that checkServerTrusted handles null certificates
		X509TrustManager trustManager = SslUtils.DisableValidationTrustManager.INSTANCE;
		Assertions.assertThatCode(() -> trustManager.checkServerTrusted(null, "RSA")).doesNotThrowAnyException();
	}

	@Test
	void test_disableValidationTrustManager_getAcceptedIssuers() {
		// Test that getAcceptedIssuers returns an empty array
		X509TrustManager trustManager = SslUtils.DisableValidationTrustManager.INSTANCE;
		X509Certificate[] issuers = trustManager.getAcceptedIssuers();
		Assertions.assertThat(issuers).isNotNull();
		Assertions.assertThat(issuers).isEmpty();
	}

	@Test
	void test_disableValidationTrustManager_constructor() {
		// Test that DisableValidationTrustManager can be instantiated
		SslUtils.DisableValidationTrustManager trustManager = new SslUtils.DisableValidationTrustManager();
		Assertions.assertThat(trustManager).isNotNull();
		Assertions.assertThat(trustManager).isInstanceOf(X509TrustManager.class);
	}

	@Test
	void test_disableValidationTrustManager_multipleInstances() {
		// Test creating multiple instances
		SslUtils.DisableValidationTrustManager tm1 = new SslUtils.DisableValidationTrustManager();
		SslUtils.DisableValidationTrustManager tm2 = new SslUtils.DisableValidationTrustManager();
		Assertions.assertThat(tm1).isNotNull();
		Assertions.assertThat(tm2).isNotNull();
		// Different instances but same behavior
		Assertions.assertThat(tm1).isNotSameAs(tm2);
	}

	@Test
	void test_sslContext_withDifferentAuthTypes() {
		// Test that the trust manager works with different auth types
		X509TrustManager trustManager = SslUtils.DisableValidationTrustManager.INSTANCE;
		X509Certificate[] certificates = new X509Certificate[0];

		Assertions.assertThatCode(() -> trustManager.checkClientTrusted(certificates, "RSA"))
			.doesNotThrowAnyException();
		Assertions.assertThatCode(() -> trustManager.checkClientTrusted(certificates, "DSA"))
			.doesNotThrowAnyException();
		Assertions.assertThatCode(() -> trustManager.checkServerTrusted(certificates, "ECDSA"))
			.doesNotThrowAnyException();
	}

	@Test
	void test_ignoreSSLTrust_hostnameVerifierAcceptsAnyHostname()
			throws NoSuchAlgorithmException, KeyManagementException {
		// Test that hostname verifier accepts any hostname
		SslUtils.ignoreSSLTrust();
		Assertions.assertThat(HttpsURLConnection.getDefaultHostnameVerifier().verify("localhost", null)).isTrue();
		Assertions.assertThat(HttpsURLConnection.getDefaultHostnameVerifier().verify("example.com", null)).isTrue();
		Assertions.assertThat(HttpsURLConnection.getDefaultHostnameVerifier().verify("192.168.1.1", null)).isTrue();
		Assertions.assertThat(HttpsURLConnection.getDefaultHostnameVerifier().verify("invalid-host", null)).isTrue();
	}

	@Test
	void test_sslContext_supportedCipherSuites() throws NoSuchAlgorithmException, KeyManagementException {
		// Test that SSL context has supported cipher suites
		SSLContext sslContext = SslUtils.sslContext();
		SSLSocketFactory socketFactory = sslContext.getSocketFactory();
		String[] supportedCipherSuites = socketFactory.getSupportedCipherSuites();
		Assertions.assertThat(supportedCipherSuites).isNotNull();
		Assertions.assertThat(supportedCipherSuites).isNotEmpty();
	}

	@Test
	void test_sslContext_defaultCipherSuites() throws NoSuchAlgorithmException, KeyManagementException {
		// Test that SSL context has default cipher suites
		SSLContext sslContext = SslUtils.sslContext();
		SSLSocketFactory socketFactory = sslContext.getSocketFactory();
		String[] defaultCipherSuites = socketFactory.getDefaultCipherSuites();
		Assertions.assertThat(defaultCipherSuites).isNotNull().isNotEmpty();
	}

	@Test
	void test_ignoreSSLTrust_idempotent() throws NoSuchAlgorithmException, KeyManagementException {
		// Test that calling ignoreSSLTrust multiple times is safe
		SslUtils.ignoreSSLTrust();
		SSLSocketFactory factory1 = HttpsURLConnection.getDefaultSSLSocketFactory();

		SslUtils.ignoreSSLTrust();
		SSLSocketFactory factory2 = HttpsURLConnection.getDefaultSSLSocketFactory();

		Assertions.assertThat(factory1).isNotNull();
		Assertions.assertThat(factory2).isNotNull();
		// Both should be valid socket factories
	}

}
