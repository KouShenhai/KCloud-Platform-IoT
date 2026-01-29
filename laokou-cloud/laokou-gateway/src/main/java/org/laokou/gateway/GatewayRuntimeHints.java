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
import org.laokou.gateway.config.RequestMatcherProperties;
import org.laokou.gateway.filter.AuthFilter;
import org.laokou.gateway.repository.NacosRouteDefinitionRepository;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.SerializationHints;
import org.springframework.aot.hint.TypeReference;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * GraalVM Native Image 运行时提示配置.
 * <p>
 * 注册 GraalVM Native 所需的反射和资源信息。
 *
 * @author laokou
 */
public class GatewayRuntimeHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(@NonNull RuntimeHints hints, ClassLoader classLoader) {
		// 注册反射类
		registerReflection(hints);
		// 注册资源文件
		registerResources(hints);
		// 注册序列化
		registerSerialization(hints);
	}

	/**
	 * 注册需要反射的类.
	 * @param hints 运行时提示
	 */
	private void registerReflection(RuntimeHints hints) {
		// Spring Cloud Gateway 核心类
		hints.reflection().registerType(RouteDefinition.class, MemberCategory.values());
		hints.reflection().registerType(FilterDefinition.class, MemberCategory.values());
		hints.reflection().registerType(PredicateDefinition.class, MemberCategory.values());

		// 项目自定义类
		hints.reflection().registerType(RequestMatcherProperties.class, MemberCategory.values());
		hints.reflection().registerType(AuthFilter.class, MemberCategory.values());
		hints.reflection().registerType(NacosRouteDefinitionRepository.class, MemberCategory.values());
		hints.reflection().registerType(GatewayApp.class, MemberCategory.values());
	}

	/**
	 * 注册需要包含的资源文件.
	 * @param hints 运行时提示
	 */
	private void registerResources(RuntimeHints hints) {
		// 配置文件
		hints.resources().registerPattern("application*.yml");

		// 日志配置
		hints.resources().registerPattern("log4j2*.xml");

		// 路由配置
		hints.resources().registerPattern("router.json");
		hints.resources().registerPattern("gateway-flow.json");

		// 国际化
		hints.resources().registerPattern("i18n/*.properties");

		// SSL证书
		hints.resources().registerPattern("scg-keystore.p12");

		// 静态资源
		hints.resources().registerPattern("static/*");
	}

	/**
	 * 注册需要序列化的类.
	 * @param hints 运行时提示
	 */
	private void registerSerialization(RuntimeHints hints) {
		SerializationHints serializationHints = hints.serialization();
		serializationHints.registerType(TypeReference.of(RouteDefinition.class));
		serializationHints.registerType(TypeReference.of(FilterDefinition.class));
		serializationHints.registerType(TypeReference.of(PredicateDefinition.class));
	}

}
