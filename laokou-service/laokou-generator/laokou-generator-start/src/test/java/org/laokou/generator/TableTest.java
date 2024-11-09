/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.generator.ability.GeneratorDomainService;
import org.laokou.generator.gatewayimpl.database.TableColumnMapper;
import org.laokou.generator.gatewayimpl.database.TableMapper;
import org.laokou.generator.gatewayimpl.database.dataobject.TableColumnDO;
import org.laokou.generator.gatewayimpl.database.dataobject.TableDO;
import org.laokou.generator.model.App;
import org.laokou.generator.model.GeneratorA;
import org.laokou.generator.model.TableE;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TableTest {

	private final TableMapper tableMapper;

	private final TableColumnMapper tableColumnMapper;

	private final GeneratorDomainService generatorDomainService;

	@Test
	void testTable() {
		List<TableDO> tables = tableMapper.selectObjects("boot_sys_user");
		log.info("获取表：{}", JacksonUtil.toJsonStr(tables));
	}

	@Test
	void testTableColumn() {
		List<TableColumnDO> tableColumns = tableColumnMapper.selectObjects("boot_sys_user");
		log.info("获取字段：{}", JacksonUtil.toJsonStr(tableColumns));
	}

	@Test
	void testMasterGenerateCode() {
		// 已注释代码生成【跑CI已注释】
		// 已注释代码生成【跑CI已注释】
		// 已注释代码生成【跑CI已注释】
		// 数据源
		String sourceName = "master";
		// 版本号
		String version = "v3";
		// 作者
		String author = "laokou";
		// 表前缀
		String tablePrefix = "boot_sys_";
		// 模块名
		String moduleName = "laokou-admin";
		// 包名
		String packageName = "org.laokou.admin";
		// 应用
		App app = App.SYS;
		// 表名
		Set<String> tableNames = Set.of("boot_sys_user", "boot_sys_menu", "boot_sys_tenant");
		generateCode(sourceName, version, author, tablePrefix, moduleName, packageName, tableNames, app);
	}

	@Test
	void testDomainGenerateCode() {
		// 已注释代码生成【跑CI已注释】
		// 已注释代码生成【跑CI已注释】
		// 已注释代码生成【跑CI已注释】
		// 数据源
		String sourceName = "domain";
		// 版本号
		String version = "v3";
		// 作者
		String author = "laokou";
		// 表前缀
		String tablePrefix = "boot_";
		// 模块名
		String moduleName = "laokou-admin";
		// 包名
		String packageName = "org.laokou.admin";
		// 应用
		App app = App.SYS;
		// 表名
		Set<String> tableNames = Set.of("boot_domain_event");
		generateCode(sourceName, version, author, tablePrefix, moduleName, packageName, tableNames, app);
	}

	@Test
	void testIotGenerateCode() {
		// 已注释代码生成【跑CI已注释】
		// 已注释代码生成【跑CI已注释】
		// 已注释代码生成【跑CI已注释】
		// 数据源
		String sourceName = "iot";
		// 版本号
		String version = "v3";
		// 作者
		String author = "laokou";
		// 表前缀
		String tablePrefix = "boot_iot_";
		// 模块名
		String moduleName = "laokou-iot";
		// 包名
		String packageName = "org.laokou.iot";
		// 应用
		App app = App.IOT;
		// 表名
		Set<String> tableNames = Set.of("boot_iot_device", "boot_iot_model", "boot_iot_network", "boot_iot_product",
				"boot_iot_product_type", "boot_iot_protocol");
		generateCode(sourceName, version, author, tablePrefix, moduleName, packageName, tableNames, app);
	}

	@Test
	void testGeneratorGenerateCode() {
		// 已注释代码生成【跑CI已注释】
		// 已注释代码生成【跑CI已注释】
		// 已注释代码生成【跑CI已注释】
		// 数据源
		String sourceName = "generator";
		// 版本号
		String version = "v3";
		// 作者
		String author = "laokou";
		// 表前缀
		String tablePrefix = "boot_generator_";
		// 模块名
		String moduleName = "laokou-generator";
		// 包名
		String packageName = "org.laokou.generator";
		// 应用
		App app = App.GENERATOR;
		// 表名
		Set<String> tableNames = Set.of("boot_generator_template", "boot_generator_info");
		generateCode(sourceName, version, author, tablePrefix, moduleName, packageName, tableNames, app);
	}

	private void generateCode(String sourceName, String version, String author, String tablePrefix, String moduleName,
			String packageName, Set<String> tableNames, App app) {
		try (ExecutorService executor = ThreadUtil.newVirtualTaskExecutor()) {
			tableNames.stream().map(item -> CompletableFuture.runAsync(() -> {
				TableE tableE = new TableE(item, tablePrefix, sourceName);
				GeneratorA generatorA = new GeneratorA(author, packageName, moduleName, version, tableE, app);
				// 已注释代码生成【跑CI已注释】
				// 已注释代码生成【跑CI已注释】
				// 已注释代码生成【跑CI已注释】
				generatorDomainService.generateCode(generatorA);
			}, executor)).forEach(CompletableFuture::join);
		}
	}

}
