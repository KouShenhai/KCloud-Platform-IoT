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

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

/**
 * laokou-gateway 架构测试.
 * <p>
 * 使用 ArchUnit 验证网关模块的架构约束.
 *
 * @author laokou
 */
@AnalyzeClasses(packages = "org.laokou.gateway", importOptions = ImportOption.DoNotIncludeTests.class)
@DisplayName("Gateway Architecture Tests")
class ArchitectureTest {

	/**
	 * 过滤器类应该在 filter 包中.
	 */
	@ArchTest
	static final ArchRule filters_should_be_in_filter_package = ArchRuleDefinition.classes().that()
		.haveNameMatching(".*Filter")
		.should()
		.resideInAPackage("..filter..");

	/**
	 * 配置类应该在 config 包中.
	 */
	@ArchTest
	static final ArchRule config_classes_should_be_in_config_package = ArchRuleDefinition.classes().that()
		.haveNameMatching(".*Config")
		.or()
		.haveNameMatching(".*Properties")
		.should()
		.resideInAPackage("..config..");

	/**
	 * 异常处理器应该在 exception.handler 包中.
	 */
	@ArchTest
	static final ArchRule exception_handlers_should_be_in_exception_package = ArchRuleDefinition.classes().that()
		.haveNameMatching(".*ExceptionHandler")
		.should()
		.resideInAPackage("..exception.handler..");

	/**
	 * 仓储类应该在 repository 包中.
	 */
	@ArchTest
	static final ArchRule repositories_should_be_in_repository_package = ArchRuleDefinition.classes().that()
		.haveNameMatching(".*Repository")
		.should()
		.resideInAPackage("..repository..");

	/**
	 * 工具类应该在 util 包中.
	 */
	@ArchTest
	static final ArchRule utils_should_be_in_util_package = ArchRuleDefinition.classes().that()
		.haveNameMatching(".*Utils")
		.should()
		.resideInAPackage("..util..");

	/**
	 * 常量类应该在 constant 包中.
	 */
	@ArchTest
	static final ArchRule constants_should_be_in_constant_package = ArchRuleDefinition.classes().that()
		.haveNameMatching(".*Constants")
		.should()
		.resideInAPackage("..constant..");

	/**
	 * 过滤器不应该直接依赖仓储实现.
	 */
	@ArchTest
	static final ArchRule filters_should_not_depend_on_repositories = ArchRuleDefinition.noClasses().that()
		.resideInAPackage("..filter..")
		.should()
		.dependOnClassesThat()
		.resideInAPackage("..repository..");

	/**
	 * 配置类不应该依赖过滤器.
	 */
	@ArchTest
	static final ArchRule config_should_not_depend_on_filters = ArchRuleDefinition.noClasses().that()
		.resideInAPackage("..config..")
		.should()
		.dependOnClassesThat()
		.resideInAPackage("..filter..");

	/**
	 * 工具类不应该依赖业务类.
	 */
	@ArchTest
	static final ArchRule utils_should_not_depend_on_business_classes = ArchRuleDefinition.noClasses().that()
		.resideInAPackage("..util..")
		.should()
		.dependOnClassesThat()
		.resideInAPackage("..filter..");

	/**
	 * 分层架构验证.
	 */
	@ArchTest
	static final ArchRule layer_dependencies_are_respected = Architectures.layeredArchitecture().consideringAllDependencies()
		.layer("Config")
		.definedBy("..config..")
		.layer("Filter")
		.definedBy("..filter..")
		.layer("Repository")
		.definedBy("..repository..")
		.layer("Util")
		.definedBy("..util..")
		.layer("Constant")
		.definedBy("..constant..")
		.layer("Exception")
		.definedBy("..exception..")
		.whereLayer("Filter")
		.mayOnlyBeAccessedByLayers("Config")
		.whereLayer("Util")
		.mayOnlyBeAccessedByLayers("Filter", "Exception", "Repository", "Config")
		.whereLayer("Constant")
		.mayOnlyBeAccessedByLayers("Repository", "Filter", "Exception", "Config");

	/**
	 * 无循环依赖.
	 */
	@ArchTest
	static final ArchRule no_cycles_in_packages = SlicesRuleDefinition.slices().matching("org.laokou.gateway.(*)..")
		.should()
		.beFreeOfCycles();

	/**
	 * 公共类应该有 Javadoc.
	 */
	@ArchTest
	static final ArchRule public_classes_should_be_documented = ArchRuleDefinition.classes().that()
		.arePublic()
		.and()
		.areNotAnonymousClasses()
		.and()
		.doNotHaveSimpleName("package-info")
		.should()
		.bePublic();

	/**
	 * 过滤器应该实现 GlobalFilter 或 WebFilter 接口.
	 */
	@ArchTest
	static final ArchRule filters_should_implement_filter_interface = ArchRuleDefinition.classes().that()
		.haveNameMatching(".*Filter")
		.and()
		.areNotInterfaces()
		.and()
		.resideInAPackage("..filter..")
		.should()
		.beAssignableTo(org.springframework.cloud.gateway.filter.GlobalFilter.class);

	/**
	 * 仓储应该实现 RouteDefinitionRepository 接口.
	 */
	@ArchTest
	static final ArchRule repositories_should_implement_repository_interface = ArchRuleDefinition.classes().that()
		.haveNameMatching(".*RouteDefinitionRepository")
		.and()
		.areNotInterfaces()
		.should()
		.beAssignableTo(org.springframework.cloud.gateway.route.RouteDefinitionRepository.class);

}
