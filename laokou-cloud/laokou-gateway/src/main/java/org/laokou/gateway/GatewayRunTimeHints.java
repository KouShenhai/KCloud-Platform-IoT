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

package org.laokou.gateway;

import org.jspecify.annotations.NonNull;
import org.laokou.gateway.config.CorsConfig;
import org.laokou.gateway.config.RequestMatcherProperties;
import org.laokou.gateway.constant.GatewayConstants;
import org.laokou.gateway.exception.handler.ExceptionHandler;
import org.laokou.gateway.filter.AuthFilter;
import org.laokou.gateway.repository.NacosRouteDefinitionRepository;
import org.laokou.gateway.util.ReactiveI18nUtils;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * Gateway AOT 运行时提示注册器.
 * <p>
 * 为 GraalVM Native Image 编译提供必要的反射、序列化和资源提示.
 * </p>
 *
 * @author laokou
 */
final class GatewayRunTimeHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(@NonNull RuntimeHints hints, ClassLoader classLoader) {
		// 注册资源文件
		registerResources(hints);
		// 注册反射类型
		registerReflection(hints);
		// 注册序列化类型
		registerSerialization(hints);
		// 注册代理
		registerProxies(hints);
	}

	/**
	 * 注册资源文件提示.
	 * @param hints 运行时提示
	 */
	private void registerResources(@NonNull RuntimeHints hints) {
		// yaml 配置文件
		hints.resources().registerPattern("application*.yml");
		// log4j2 配置文件
		hints.resources().registerPattern("log4j2*.xml");
		// 静态配置
		hints.resources().registerPattern("static/*.ico");
		// i18n 国际化资源文件
		hints.resources().registerPattern("i18n/*.properties");
		// 动态路由配置文件
		hints.resources().registerPattern("router.json");
		// Sentinel 网关流控规则配置
		hints.resources().registerPattern("gateway-flow.json");
		// SSL 证书
		hints.resources().registerPattern("*.p12");
		// Banner
		hints.resources().registerPattern("banner.txt");
		// 自动装配 相关配置
		hints.resources().registerPattern("META-INF/spring/*.imports");
		hints.resources().registerPattern("META-INF/spring/*.factories");
		// Native Image 配置
		hints.resources().registerPattern("META-INF/native-image/**");
	}

	/**
	 * 注册反射类型提示.
	 * @param hints 运行时提示
	 */
	private void registerReflection(RuntimeHints hints) {
		// Gateway 路由定义相关类
		registerReflectionType(hints, RouteDefinition.class);
		registerReflectionType(hints, FilterDefinition.class);
		registerReflectionType(hints, PredicateDefinition.class);
		// Gateway 本地类 - 配置类
		registerReflectionType(hints, RequestMatcherProperties.class);
		registerReflectionType(hints, CorsConfig.class);
		// Gateway 本地类 - 过滤器
		registerReflectionType(hints, AuthFilter.class);
		// Gateway 本地类 - 仓库
		registerReflectionType(hints, NacosRouteDefinitionRepository.class);
		// Gateway 本地类 - 异常处理器
		registerReflectionType(hints, ExceptionHandler.class);
		// Gateway 本地类 - 常量类
		registerReflectionType(hints, GatewayConstants.class);
		// Gateway 本地类 - 工具类
		registerReflectionType(hints, ReactiveI18nUtils.class);
		// Gateway 本地类 - 应用启动类
		registerReflectionType(hints, GatewayApp.class);
	}

	/**
	 * 注册序列化类型提示.
	 * @param hints 运行时提示
	 */
	private void registerSerialization(RuntimeHints hints) {
		// Gateway 路由定义序列化支持
		hints.serialization().registerType(TypeReference.of(RouteDefinition.class));
		hints.serialization().registerType(TypeReference.of(FilterDefinition.class));
		hints.serialization().registerType(TypeReference.of(PredicateDefinition.class));
		// Java 核心集合类型序列化支持
		hints.serialization().registerType(TypeReference.of(java.util.ArrayList.class));
		hints.serialization().registerType(TypeReference.of(java.util.LinkedHashMap.class));
		hints.serialization().registerType(TypeReference.of(java.util.HashMap.class));
		hints.serialization().registerType(TypeReference.of(java.util.HashSet.class));
		hints.serialization().registerType(TypeReference.of(java.util.LinkedHashSet.class));
		// URI 序列化支持
		hints.serialization().registerType(TypeReference.of(java.net.URI.class));
	}

	/**
	 * 注册代理配置.
	 * @param hints 运行时提示
	 */
	private void registerProxies(RuntimeHints hints) {
		// Spring Cloud Gateway 相关代理
		hints.proxies().registerJdkProxy(org.springframework.cloud.gateway.route.RouteDefinitionRepository.class);
		hints.proxies().registerJdkProxy(org.springframework.cloud.gateway.filter.GlobalFilter.class);
		hints.proxies().registerJdkProxy(org.springframework.web.server.WebFilter.class);
		hints.proxies().registerJdkProxy(org.springframework.boot.webflux.error.ErrorWebExceptionHandler.class);
		// Nacos 配置监听器代理
		hints.proxies().registerJdkProxy(com.alibaba.nacos.api.config.listener.Listener.class);
	}

	/**
	 * 注册反射类型（包含所有成员类别）.
	 * @param hints 运行时提示
	 * @param type 类型
	 */
	private void registerReflectionType(RuntimeHints hints, Class<?> type) {
		hints.reflection()
			.registerType(type,
					builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
							MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
							MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.ACCESS_DECLARED_FIELDS,
							MemberCategory.ACCESS_PUBLIC_FIELDS));
	}

}
