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
		generateCode(version, author, tablePrefix, moduleName, packageName, tableNames, app);
	}

	@Test
	void testDomainGenerateCode() {

		// 注意：需要手动切换数据源至domain

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
		generateCode(version, author, tablePrefix, moduleName, packageName, tableNames, app);
	}

	@Test
	void testIotGenerateCode() {

		// 注意：需要手动切换数据源至iot

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
		Set<String> tableNames = Set.of("boot_iot_device");
		generateCode(version, author, tablePrefix, moduleName, packageName, tableNames, app);
	}

	private void generateCode(String version, String author, String tablePrefix, String moduleName, String packageName,
			Set<String> tableNames, App app) {
		tableNames.stream().map(item -> CompletableFuture.runAsync(() -> {
			TableE tableE = new TableE(item, tablePrefix);
			GeneratorA generatorA = new GeneratorA(author, packageName, moduleName, version, tableE, app);
			generatorDomainService.generateCode(generatorA);
		})).forEach(CompletableFuture::join);
	}

}
