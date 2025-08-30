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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.DateUtils;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.TransactionDefinition;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TransactionalUtilsTest {

	private final TestUserMapper testUserMapper;

	private final TransactionalUtils transactionalUtils;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
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
	void test_withoutResult() {
		assertThatNoException()
			.isThrownBy(() -> transactionalUtils.executeInTransaction(() -> testUserMapper.insert(getTestUserDO())));
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 9L)))
			.isEqualTo(1);
		assertThatNoException()
			.isThrownBy(() -> transactionalUtils.executeInTransaction(() -> testUserMapper.deleteTestUser(List.of(9L)),
					TransactionDefinition.ISOLATION_READ_COMMITTED, false));
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 9L)))
			.isEqualTo(0);
		assertThatNoException()
			.isThrownBy(() -> transactionalUtils.executeInNewTransaction(() -> testUserMapper.insert(getTestUserDO())));
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 9L)))
			.isEqualTo(1);
		assertThatNoException().isThrownBy(
				() -> transactionalUtils.executeInNewTransaction(() -> testUserMapper.deleteTestUser(List.of(9L)),
						TransactionDefinition.ISOLATION_READ_COMMITTED, false));
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 9L)))
			.isEqualTo(0);
		assertThatNoException()
			.isThrownBy(() -> transactionalUtils.executeInTransaction(() -> testUserMapper.insert(getTestUserDO()),
					TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.ISOLATION_READ_COMMITTED, false));
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 9L)))
			.isEqualTo(1);
		assertThatNoException()
			.isThrownBy(() -> transactionalUtils.executeInTransaction(() -> testUserMapper.deleteTestUser(List.of(9L)),
					TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.ISOLATION_READ_COMMITTED,
					false));
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 9L)))
			.isEqualTo(0);
	}

	@Test
	void test_result() {
		int count = transactionalUtils.executeResultInNewTransaction(() -> testUserMapper.insert(getTestUserDO2()));
		assertThat(count > 0).isTrue();
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 10L)))
			.isEqualTo(1);
		count = transactionalUtils.executeResultInTransaction(() -> testUserMapper.deleteTestUser(List.of(10L)));
		assertThat(count > 0).isTrue();
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 10L)))
			.isEqualTo(0);
		count = transactionalUtils.executeResultInNewTransaction(() -> testUserMapper.insert(getTestUserDO2()),
				TransactionDefinition.ISOLATION_READ_COMMITTED, false);
		assertThat(count > 0).isTrue();
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 10L)))
			.isEqualTo(1);
		count = transactionalUtils.executeResultInTransaction(() -> testUserMapper.deleteTestUser(List.of(10L)),
				TransactionDefinition.ISOLATION_READ_COMMITTED, false);
		assertThat(count > 0).isTrue();
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 10L)))
			.isEqualTo(0);
		count = transactionalUtils.executeResultInTransaction(() -> testUserMapper.insert(getTestUserDO2()),
				TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.ISOLATION_READ_COMMITTED, false);
		assertThat(count > 0).isTrue();
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 10L)))
			.isEqualTo(1);
		count = transactionalUtils.executeResultInTransaction(() -> testUserMapper.deleteTestUser(List.of(10L)),
				TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.ISOLATION_READ_COMMITTED, false);
		assertThat(count > 0).isTrue();
		assertThat(testUserMapper.selectCount(Wrappers.lambdaQuery(MybatisUtilsTest.TestUserDO.class).eq(MybatisUtilsTest.TestUserDO::getId, 10L)))
			.isEqualTo(0);
	}

	private MybatisUtilsTest.TestUserDO getTestUserDO() {
		MybatisUtilsTest.TestUserDO testUserDO = new MybatisUtilsTest.TestUserDO();
		testUserDO.setName("李四");
		testUserDO.setCreator(1L);
		testUserDO.setEditor(1L);
		testUserDO.setCreateTime(DateUtils.nowInstant());
		testUserDO.setUpdateTime(DateUtils.nowInstant());
		testUserDO.setVersion(0);
		testUserDO.setTenantId(0L);
		testUserDO.setDelFlag(0);
		testUserDO.setId(9L);
		return testUserDO;
	}

	private MybatisUtilsTest.TestUserDO getTestUserDO2() {
		MybatisUtilsTest.TestUserDO testUserDO = new MybatisUtilsTest.TestUserDO();
		testUserDO.setName("王二");
		testUserDO.setCreator(1L);
		testUserDO.setEditor(1L);
		testUserDO.setCreateTime(DateUtils.nowInstant());
		testUserDO.setUpdateTime(DateUtils.nowInstant());
		testUserDO.setVersion(0);
		testUserDO.setTenantId(0L);
		testUserDO.setDelFlag(0);
		testUserDO.setId(10L);
		return testUserDO;
	}

}
