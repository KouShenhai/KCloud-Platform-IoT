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

package org.laokou.common.core.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.laokou.common.i18n.utils.LogUtil;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import static org.laokou.common.i18n.common.StringConstants.EMPTY;
import static org.laokou.common.i18n.common.SysConstants.LINE_PATTERN;
import static org.laokou.common.i18n.common.SysConstants.TLS_PROTOCOL_VERSION;

/**
 * http客户端工具类.
 * @author laokou
 */
@Slf4j
public class HttpUtil {

	public static String doGet(String url, Map<String, String> params, Map<String, String> headers) {
		return doGet(url, params, headers, false);
	}

	@SneakyThrows
	public static String doGet(String url, Map<String, String> params, Map<String, String> headers,
			boolean disableSsl) {
		// 创建HttpClient对象
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		if (disableSsl) {
			disableSsl(httpClientBuilder);
		}
		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (MapUtil.isNotEmpty(params)) {
				params.forEach(builder::addParameter);
			}
			URI uri = builder.build();
			// 创建GET请求
			HttpGet httpGet = new HttpGet(uri);
			if (MapUtil.isNotEmpty(headers)) {
				headers.forEach(httpGet::addHeader);
			}
			httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
			httpGet.setHeader("Accept", "*/*;charset=utf-8");
			String resultString = EMPTY;
			try {
				// 执行请求
				resultString = httpClient.execute(httpGet,
						handler -> EntityUtils.toString(handler.getEntity(), StandardCharsets.UTF_8));
			}
			catch (Exception e) {
				log.error("调用失败，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
			}
			// log.info("打印：{}", resultString);
			return resultString;
		}
	}

	@SneakyThrows
	public static String doFormDataPost(String url, Map<String, String> params, Map<String, String> headers,
			boolean disableSsl) {
		// 创建HttpClient对象
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		if (disableSsl) {
			disableSsl(httpClientBuilder);
		}
		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
			HttpPost httpPost = new HttpPost(url);
			if (MapUtil.isNotEmpty(headers)) {
				headers.forEach(httpPost::addHeader);
			}
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			if (MapUtil.isNotEmpty(params)) {
				params.forEach(entityBuilder::addTextBody);
			}
			HttpEntity httpEntity = entityBuilder.build();
			httpPost.setEntity(httpEntity);
			String resultString = EMPTY;
			try {
				// 执行请求
				resultString = httpClient.execute(httpPost,
						handler -> EntityUtils.toString(handler.getEntity(), StandardCharsets.UTF_8));
			}
			catch (IOException e) {
				log.error("调用失败，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
			}
			// log.info("打印：{}", resultString);
			return resultString;
		}
	}

	public static String doFormUrlencodedPost(String url, Map<String, String> params, Map<String, String> headers) {
		return doFormUrlencodedPost(url, params, headers, false);
	}

	@SneakyThrows
	public static String doFormUrlencodedPost(String url, Map<String, String> params, Map<String, String> headers,
			boolean disableSsl) {
		// 创建HttpClient对象
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		if (disableSsl) {
			disableSsl(httpClientBuilder);
		}
		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
			// 创建Post请求
			HttpPost httpPost = new HttpPost(url);
			if (MapUtil.isNotEmpty(headers)) {
				headers.forEach(httpPost::addHeader);
			}
			// 创建参数列表
			if (MapUtil.isNotEmpty(params)) {
				List<BasicNameValuePair> list = params.entrySet()
					.stream()
					.map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
					.toList();
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, StandardCharsets.UTF_8);
				httpPost.setEntity(entity);
			}
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			httpPost.setHeader("Accept", "*/*;charset=utf-8");
			String resultString = EMPTY;
			try {
				// 执行请求
				resultString = httpClient.execute(httpPost,
						handler -> EntityUtils.toString(handler.getEntity(), StandardCharsets.UTF_8));
			}
			catch (IOException e) {
				log.error("调用失败，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
			}
			// log.info("打印：{}", resultString);
			return resultString;
		}
	}

	public static String doJsonPost(String url, Object param, Map<String, String> headers) {
		return doJsonPost(url, param, headers, false);
	}

	@SneakyThrows
	public static String doJsonPost(String url, Object param, Map<String, String> headers, boolean disableSsl) {
		// 创建HttpClient对象
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		if (disableSsl) {
			disableSsl(httpClientBuilder);
		}
		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
			HttpPost httpPost = new HttpPost(url);
			headers.forEach(httpPost::setHeader);
			httpPost.setConfig(RequestConfig.custom().build());
			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.setHeader("Accept", "*/*;charset=utf-8");
			String parameter = JacksonUtil.toJsonStr(param);
			httpPost.setEntity(new StringEntity(parameter));
			String resultString = EMPTY;
			try {
				// 执行请求
				resultString = httpClient.execute(httpPost,
						handler -> EntityUtils.toString(handler.getEntity(), StandardCharsets.UTF_8));
			}
			catch (IOException e) {
				log.error("调用失败，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
			}
			// log.info("打印：{}", resultString);
			return resultString;
		}
	}

	/**
	 * 转换为驼峰json字符串.
	 */
	public static String transformerUnderHumpData(String data) {
		Matcher matcher = LINE_PATTERN.matcher(data.toLowerCase());
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 关闭ssl校验.
	 */
	@SneakyThrows
	public static void disableSsl(HttpClientBuilder builder) {
		SSLContext sslContext = sslContext();
		SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder.create()
			.setSslContext(sslContext)
			.setHostnameVerifier(new TrustAllHostnames())
			.build();
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder
			.create()
			.setSSLSocketFactory(sslConnectionSocketFactory)
			.build();
		builder.setConnectionManager(poolingHttpClientConnectionManager);
	}

	@SneakyThrows
	public static SSLContext sslContext() {
		// X.509是密码学里公钥证书的格式标准，作为证书标准
		X509TrustManager disabledTrustManager = new DisableValidationTrustManager();
		// 信任库
		TrustManager[] trustManagers = new TrustManager[] { disabledTrustManager };
		// 怎么选择加密协议，请看 ProtocolVersion
		// 为什么能找到对应的加密协议 请查看 SSLContextSpi
		SSLContext sslContext = SSLContext.getInstance(TLS_PROTOCOL_VERSION);
		sslContext.init(null, trustManagers, new SecureRandom());
		return sslContext;
	}

	public static class DisableValidationTrustManager implements X509TrustManager {

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

	static class TrustAllHostnames implements HostnameVerifier {

		TrustAllHostnames() {
		}

		public boolean verify(String s, SSLSession sslSession) {
			return true;
		}

	}

}
