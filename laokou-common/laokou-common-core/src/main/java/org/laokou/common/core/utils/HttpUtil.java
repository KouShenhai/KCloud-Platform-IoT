/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.laokou.common.i18n.utils.LogUtil;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.laokou.common.core.utils.SslUtil.sslContext;
import static org.laokou.common.i18n.common.StringConstant.EMPTY;

/**
 * http客户端工具类.
 *
 * @author laokou
 */
@Slf4j
public class HttpUtil {

	/**
	 * get请求.
	 * @param url 链接
	 * @param params 参数
	 * @param headers 请求头
	 * @return 响应结果
	 */
	public static String doGet(String url, Map<String, String> params, Map<String, String> headers) {
		return doGet(url, params, headers, false);
	}

	/**
	 * get请求.
	 * @param url 链接
	 * @param params 参数
	 * @param headers 请求头
	 * @param disableSsl ssl开关
	 * @return 响应结果
	 */
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
				log.error("调用失败，错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
			}
			return resultString;
		}
	}

	/**
	 * 表单提交.
	 * @param url 链接
	 * @param params 参数
	 * @param headers 请求头
	 * @param disableSsl ssl开关
	 * @return 响应结果
	 */
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
				log.error("调用失败，错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
			}
			return resultString;
		}
	}

	/**
	 * 表单提交.
	 * @param url 链接
	 * @param params 参数
	 * @param headers 请求头
	 * @return 响应结果
	 */
	public static String doFormUrlencodedPost(String url, Map<String, String> params, Map<String, String> headers) {
		return doFormUrlencodedPost(url, params, headers, false);
	}

	/**
	 * post请求.
	 * @param url 链接
	 * @param params 参数
	 * @param headers 请求头
	 * @param disableSsl ssl开关
	 * @return 响应结果
	 */
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
				log.error("调用失败，错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
			}
			return resultString;
		}
	}

	/**
	 * post请求（json）.
	 * @param url 链接
	 * @param param 参数
	 * @param headers 请求头
	 * @return 响应结果
	 */
	public static String doJsonPost(String url, Object param, Map<String, String> headers) {
		return doJsonPost(url, param, headers, false);
	}

	/**
	 * post请求（json）.
	 * @param url 链接
	 * @param param 参数
	 * @param headers 请求头
	 * @param disableSsl ssl开关
	 * @return 响应结果
	 */
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
				log.error("调用失败，错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
			}
			return resultString;
		}
	}

	/**
	 * 关闭ssl校验.
	 * @param builder 构建器
	 */
	@SneakyThrows
	private static void disableSsl(HttpClientBuilder builder) {
		SSLContext sslContext = sslContext();
		SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder.create()
			.setSslContext(sslContext)
			.setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
			.build();
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder
			.create()
			.setSSLSocketFactory(sslConnectionSocketFactory)
			.build();
		builder.setConnectionManager(poolingHttpClientConnectionManager);
	}

}
