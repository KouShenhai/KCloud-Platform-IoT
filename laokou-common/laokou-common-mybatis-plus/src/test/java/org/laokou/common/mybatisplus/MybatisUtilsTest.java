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

package org.laokou.common.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.mybatisplus.config.GlobalTenantLineHandler;
import org.laokou.common.mybatisplus.mapper.BaseDO;
import org.laokou.common.mybatisplus.util.MybatisUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Set;

/**
 * @author laokou
 */
@SpringBootConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@MapperScan(basePackages = "org.laokou.common.mybatisplus")
@SpringBootApplication(scanBasePackages = "org.laokou")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MybatisUtilsTest {

	private final TestUserMapper testUserMapper;

	private final MybatisUtils mybatisUtils;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest").withUsername("root")
		.withPassword("laokou123")
		.withInitScript("init.sql")
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
	void test_ignoreTable() {
		GlobalTenantLineHandler globalTenantLineHandler = new GlobalTenantLineHandler(Set.of("test", "t_user"));
		Assertions.assertThat(globalTenantLineHandler.ignoreTable("t_user")).isTrue();
		Assertions.assertThat(globalTenantLineHandler.ignoreTable("t_test")).isFalse();
		Assertions.assertThat(globalTenantLineHandler.ignoreTable("t_tes1")).isFalse();
	}

	@Test
	void test_mybatisPlus() {
		Assertions.assertThat(testUserMapper).isNotNull();
		Assertions.assertThat(mybatisUtils).isNotNull();
		Assertions.assertThatNoException().isThrownBy(testUserMapper::deleteAllUser);
		Assertions.assertThatNoException()
			.isThrownBy(() -> mybatisUtils.batch(List.of(getTestUserDO(), getTestUser1DO()), TestUserMapper.class,
					TestUserMapper::insert));
		List<TestUserDO> list = testUserMapper.selectList(Wrappers.emptyWrapper());
		Assertions.assertThat(list.size()).isEqualTo(2);
		Assertions.assertThat(list.stream().map(TestUserDO::getName).anyMatch("张三"::equals)).isTrue();
		Assertions.assertThatNoException().isThrownBy(() -> testUserMapper.deleteTestUser(List.of(8L)));
		Assertions.assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(1);
		Assertions.assertThatNoException()
			.isThrownBy(() -> mybatisUtils.batch(List.of(getTestUserDO()), TestUserMapper.class, "master",
					TestUserMapper::insert));
		Assertions.assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(2);
		Assertions.assertThatNoException().isThrownBy(() -> testUserMapper.deleteTestUser(List.of(8L)));
		Assertions.assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(1);
		Assertions.assertThatNoException()
			.isThrownBy(() -> mybatisUtils.batch(List.of(getTestUserDO()), 1000, 100, 180, TestUserMapper.class,
					"master", TestUserMapper::insert));
		Assertions.assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(2);
		Assertions.assertThatNoException().isThrownBy(() -> testUserMapper.deleteTestUser(List.of(8L)));
		Assertions.assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(1);
		Assertions.assertThatNoException()
			.isThrownBy(() -> mybatisUtils.batch(List.of(getTestUserDO()), 1000, 100, 180, TestUserMapper.class,
					TestUserMapper::insert));
		Assertions.assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(2);
		Assertions.assertThatNoException().isThrownBy(() -> testUserMapper.deleteTestUser(List.of(8L)));
		Assertions.assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(1);
	}

	private TestUserDO getTestUserDO() {
		TestUserDO testUserDO = new TestUserDO();
		testUserDO.setName("张三");
		testUserDO.setCreator(1L);
		testUserDO.setEditor(1L);
		testUserDO.setCreateTime(InstantUtils.now());
		testUserDO.setUpdateTime(InstantUtils.now());
		testUserDO.setVersion(0);
		testUserDO.setTenantId(0L);
		testUserDO.setDelFlag(0);
		testUserDO.setId(8L);
		testUserDO.setDeptId(1L);
		return testUserDO;
	}

	private TestUserDO getTestUser1DO() {
		TestUserDO testUserDO = new TestUserDO();
		testUserDO.setName("李四");
		testUserDO.setCreator(1L);
		testUserDO.setEditor(1L);
		testUserDO.setCreateTime(InstantUtils.now());
		testUserDO.setUpdateTime(InstantUtils.now());
		testUserDO.setVersion(0);
		testUserDO.setTenantId(0L);
		testUserDO.setDelFlag(0);
		testUserDO.setId(20L);
		testUserDO.setDeptId(1L);
		return testUserDO;
	}

	@Data
	@TableName("t_user")
	static class TestUserDO extends BaseDO {

		private String name;

	}

}
