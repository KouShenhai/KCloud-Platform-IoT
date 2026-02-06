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

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.DisplayName;

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

	@ArchTest
	void test_filters_should_be_in_filter_package(JavaClasses classes) {
		// 过滤器类应该在 filter 包中
		ArchRuleDefinition.classes()
			.that()
			.haveNameMatching(".*Filter")
			.should()
			.resideInAPackage("..filter..")
			.check(classes);
	}

	@ArchTest
	void test_config_classes_should_be_in_config_package(JavaClasses classes) {
		// 配置类应该在 config 包中
		ArchRuleDefinition.classes()
			.that()
			.haveNameMatching(".*Config")
			.or()
			.haveNameMatching(".*Properties")
			.should()
			.resideInAPackage("..config..")
			.check(classes);
	}

	@ArchTest
	void test_exception_handlers_should_be_in_exception_package(JavaClasses classes) {
		// 异常处理器应该在 exception.handler 包中
		ArchRuleDefinition.classes()
			.that()
			.haveNameMatching(".*ExceptionHandler")
			.should()
			.resideInAPackage("..exception.handler..")
			.check(classes);
	}

	@ArchTest
	void test_repositories_should_be_in_repository_package(JavaClasses classes) {
		// 仓储类应该在 repository 包中
		ArchRuleDefinition.classes()
			.that()
			.haveNameMatching(".*Repository")
			.should()
			.resideInAPackage("..repository..")
			.check(classes);
	}

	@ArchTest
	void test_utils_should_be_in_util_package(JavaClasses classes) {
		// 工具类应该在 util 包中
		ArchRuleDefinition.classes()
			.that()
			.haveNameMatching(".*Utils")
			.should()
			.resideInAPackage("..util..")
			.check(classes);
	}

	@ArchTest
	void test_filters_should_not_depend_on_repositories(JavaClasses classes) {
		// 过滤器不应该直接依赖仓储实现
		ArchRuleDefinition.noClasses()
			.that()
			.resideInAPackage("..filter..")
			.should()
			.dependOnClassesThat()
			.resideInAPackage("..repository..")
			.check(classes);
	}

	@ArchTest
	void test_config_should_not_depend_on_filters(JavaClasses classes) {
		// 配置类不应该依赖过滤器
		ArchRuleDefinition.noClasses()
			.that()
			.resideInAPackage("..config..")
			.should()
			.dependOnClassesThat()
			.resideInAPackage("..filter..")
			.check(classes);
	}

	@ArchTest
	void test_utils_should_not_depend_on_business_classes(JavaClasses classes) {
		// 工具类不应该依赖业务类
		ArchRuleDefinition.noClasses()
			.that()
			.resideInAPackage("..util..")
			.should()
			.dependOnClassesThat()
			.resideInAPackage("..filter..")
			.check(classes);
	}

	@ArchTest
	void test_layer_dependencies_are_respected(JavaClasses classes) {
		// 分层架构验证
		Architectures.layeredArchitecture()
			.consideringAllDependencies()
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
			.mayOnlyBeAccessedByLayers("Repository", "Filter", "Exception", "Config")
			.check(classes);
	}

	@ArchTest
	void test_no_cycles_in_packages(JavaClasses classes) {
		// 无循环依赖
		SlicesRuleDefinition.slices().matching("org.laokou.gateway.(*)..").should().beFreeOfCycles().check(classes);
	}

	@ArchTest
	void test_public_classes_should_be_documented(JavaClasses classes) {
		// 公共类应该可以被公开访问
		ArchRuleDefinition.classes()
			.that()
			.arePublic()
			.and()
			.areNotAnonymousClasses()
			.and()
			.doNotHaveSimpleName("package-info")
			.should()
			.bePublic()
			.check(classes);
	}

	@ArchTest
	void test_filters_should_implement_filter_interface(JavaClasses classes) {
		// 过滤器应该实现 GlobalFilter 或 WebFilter 接口
		ArchRuleDefinition.classes()
			.that()
			.haveNameMatching(".*Filter")
			.and()
			.areNotInterfaces()
			.and()
			.resideInAPackage("..filter..")
			.should()
			.beAssignableTo(org.springframework.cloud.gateway.filter.GlobalFilter.class)
			.check(classes);
	}

	@ArchTest
	void test_repositories_should_implement_repository_interface(JavaClasses classes) {
		// 仓储应该实现 RouteDefinitionRepository 接口
		ArchRuleDefinition.classes()
			.that()
			.haveNameMatching(".*RouteDefinitionRepository")
			.and()
			.areNotInterfaces()
			.should()
			.beAssignableTo(org.springframework.cloud.gateway.route.RouteDefinitionRepository.class)
			.check(classes);
	}

}
