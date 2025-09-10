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

package org.laokou.common.core.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.SslUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author laokou
 */
@Slf4j
public final class OkHttpUtils {

	private static final OkHttpClient INSTANCE;

	static {
		try {
			INSTANCE = getOkHttpClient();
		}
		catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.error("SSL初始化失败，错误信息：{}", e.getMessage(), e);
			throw new SystemException("S_OkHttp_SslInitFailed", "SSL初始化失败", e);
		}
	}

	private OkHttpUtils() {
	}

	public static String doFormDataPost(String url, Map<String, String> params, Map<String, String> headers) {
		FormBody.Builder builder = new FormBody.Builder();
		if (MapUtils.isNotEmpty(params)) {
			params.forEach(builder::add);
		}
		Request request = new Request.Builder().url(url).headers(Headers.of(headers)).post(builder.build()).build();
		try (Response response = INSTANCE.newCall(request).execute()) {
			ResponseBody body = response.body();
			return ObjectUtils.isNotNull(body) ? body.string() : StringConstants.EMPTY;
		}
		catch (IOException e) {
			log.error("调用失败，错误信息：{}", e.getMessage());
		}
		return StringConstants.EMPTY;
	}

	private static OkHttpClient getOkHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
		return new OkHttpClient.Builder()
			.sslSocketFactory(SslUtils.sslContext().getSocketFactory(), SslUtils.DisableValidationTrustManager.INSTANCE)
			.hostnameVerifier((hostname, session) -> true)
			.connectTimeout(Duration.ofSeconds(15))
			.readTimeout(Duration.ofSeconds(15))
			.writeTimeout(Duration.ofSeconds(15))
			.pingInterval(Duration.ZERO)
			.connectionPool(new ConnectionPool(50, Duration.ofMinutes(5).toNanos(), TimeUnit.NANOSECONDS))
			.build();
	}

}
