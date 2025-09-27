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

package org.laokou.common.excel;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.FileUtils;
import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.util.DateUtils;
import org.laokou.common.i18n.util.ResourceUtils;
import org.laokou.common.mybatisplus.util.MybatisUtils;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * @author laokou
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "org.laokou")
@MapperScan(basePackages = "org.laokou.common.excel")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ExcelTest {

	private final TestUserMapper testUserMapper;

	private final MybatisUtils mybatisUtils;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageNames.postgresql())
		.withUsername("root")
		.withPassword("laokou123")
		.withInitScripts("init.sql")
		.withDatabaseName("kcloud_platform_test");

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.dynamic.datasource.master.url", postgres::getJdbcUrl);
	}

	@Test
	void test_exportExcel() {
		Assertions.assertThat(testUserMapper).isNotNull();
		Assertions.assertThatNoException().isThrownBy(testUserMapper::deleteAllUser);
		Assertions.assertThatNoException().isThrownBy(() -> {
			TestUserDO testUserDO = new TestUserDO();
			testUserDO.setName("老寇");
			testUserDO.setId(1L);
			testUserDO.setCreator(1L);
			testUserDO.setEditor(1L);
			testUserDO.setCreateTime(DateUtils.nowInstant());
			testUserDO.setUpdateTime(DateUtils.nowInstant());
			testUserDO.setVersion(0);
			testUserDO.setTenantId(0L);
			testUserDO.setDelFlag(0);
			testUserMapper.insert(testUserDO);
		});
		List<TestUserDO> list = testUserMapper.selectList(Wrappers.emptyWrapper());
		Assertions.assertThat(list.size()).isEqualTo(1);
		Assertions.assertThat(list.stream().map(TestUserDO::getName).toList().contains("老寇")).isTrue();
		Assertions.assertThatNoException()
			.isThrownBy(() -> ExcelUtils.doImport(StringConstants.EMPTY, TestUserExcel.class,
					TestUserConvertor.INSTANCE, ResourceUtils.getResource("classpath:test3.xlsx").getInputStream(),
					TestUserMapper.class, TestUserMapper::insert, mybatisUtils));
		Assertions.assertThatNoException()
			.isThrownBy(() -> ExcelUtils.doImport("test", TestUserExcel.class, TestUserConvertor.INSTANCE,
					ResourceUtils.getResource("classpath:test2.xlsx").getInputStream(), TestUserMapper.class,
					TestUserMapper::insert, mybatisUtils));
		Assertions.assertThatNoException()
			.isThrownBy(() -> ExcelUtils.doImport("test2", TestUserExcel.class, TestUserConvertor.INSTANCE,
					ResourceUtils.getResource("classpath:test2.xlsx").getInputStream(), TestUserMapper.class,
					TestUserMapper::insert, mybatisUtils));
		Assertions.assertThatNoException()
			.isThrownBy(() -> ExcelUtils.doImport("test3", TestUserExcel.class, TestUserConvertor.INSTANCE,
					ResourceUtils.getResource("classpath:test2.xlsx").getInputStream(), TestUserMapper.class,
					TestUserMapper::insert, mybatisUtils));
		long count = testUserMapper.selectObjectCount(new PageQuery());
		Assertions.assertThat(count).isEqualTo(7);
		Assertions.assertThatNoException()
			.isThrownBy(() -> ExcelUtils.doExport("测试用户Sheet页", 1000, new FileOutputStream("test.xlsx"),
					new PageQuery(), testUserMapper, TestUserExcel.class, TestUserConvertor.INSTANCE));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.deleteIfExists(Path.of("test.xlsx")));
		Assertions.assertThatNoException().isThrownBy(() -> testUserMapper.deleteUser(List.of(2L, 3L, 4L, 5L, 6L, 7L)));
		count = testUserMapper.selectObjectCount(new PageQuery());
		Assertions.assertThat(count).isEqualTo(1);
	}

}
