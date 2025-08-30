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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.util.DateUtils;
import org.laokou.common.mybatisplus.config.GlobalTenantLineHandler;
import org.laokou.common.mybatisplus.mapper.BaseDO;
import org.laokou.common.mybatisplus.util.MybatisUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MybatisUtilsTest {

	private final TestUserMapper testUserMapper;

	private final MybatisUtils mybatisUtils;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
		.withUsername("root")
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
		assertThat(globalTenantLineHandler.ignoreTable("t_user")).isTrue();
		assertThat(globalTenantLineHandler.ignoreTable("t_test")).isFalse();
		assertThat(globalTenantLineHandler.ignoreTable("t_tes1")).isFalse();
	}

	@Test
	void test_mybatisPlus() {
		assertThat(testUserMapper).isNotNull();
		assertThat(mybatisUtils).isNotNull();
		assertThatNoException().isThrownBy(testUserMapper::deleteAllUser);
		assertThatNoException().isThrownBy(
				() -> mybatisUtils.batch(List.of(getTestUserDO(), getTestUser1DO()), TestUserMapper.class, TestUserMapper::insert));
		List<TestUserDO> list = testUserMapper.selectList(Wrappers.emptyWrapper());
		assertThat(list.size()).isEqualTo(2);
		assertThat(list.stream().map(TestUserDO::getName).anyMatch("张三"::equals)).isTrue();
		assertThatNoException().isThrownBy(() -> testUserMapper.deleteTestUser(List.of(8L)));
		assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(1);
		assertThatNoException().isThrownBy(() -> mybatisUtils.batch(List.of(getTestUserDO()), TestUserMapper.class,
				"master", TestUserMapper::insert));
		assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(2);
		assertThatNoException().isThrownBy(() -> testUserMapper.deleteTestUser(List.of(8L)));
		assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(1);
		assertThatNoException().isThrownBy(() -> mybatisUtils.batch(List.of(getTestUserDO()), 1000, 100, 180,
				TestUserMapper.class, "master", TestUserMapper::insert));
		assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(2);
		assertThatNoException().isThrownBy(() -> testUserMapper.deleteTestUser(List.of(8L)));
		assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(1);
		assertThatNoException().isThrownBy(() -> mybatisUtils.batch(List.of(getTestUserDO()), 1000, 100, 180,
				TestUserMapper.class, TestUserMapper::insert));
		assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(2);
		assertThatNoException().isThrownBy(() -> testUserMapper.deleteTestUser(List.of(8L)));
		assertThat(testUserMapper.selectObjectCount(new PageQuery())).isEqualTo(1);
	}

	private TestUserDO getTestUserDO() {
		TestUserDO testUserDO = new TestUserDO();
		testUserDO.setName("张三");
		testUserDO.setCreator(1L);
		testUserDO.setEditor(1L);
		testUserDO.setCreateTime(DateUtils.nowInstant());
		testUserDO.setUpdateTime(DateUtils.nowInstant());
		testUserDO.setVersion(0);
		testUserDO.setTenantId(0L);
		testUserDO.setDelFlag(0);
		testUserDO.setId(8L);
		return testUserDO;
	}

	private TestUserDO getTestUser1DO() {
		TestUserDO testUserDO = new TestUserDO();
		testUserDO.setName("李四");
		testUserDO.setCreator(1L);
		testUserDO.setEditor(1L);
		testUserDO.setCreateTime(DateUtils.nowInstant());
		testUserDO.setUpdateTime(DateUtils.nowInstant());
		testUserDO.setVersion(0);
		testUserDO.setTenantId(0L);
		testUserDO.setDelFlag(0);
		testUserDO.setId(20L);
		return testUserDO;
	}

	@Data
	@TableName("t_user")
	static class TestUserDO extends BaseDO {

		private String name;

	}

}
