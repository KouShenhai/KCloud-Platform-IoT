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

package org.laokou.common.starrocks;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.testcontainers.container.StarrocksContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.test.context.TestConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class StarrocksApiTest {

	static final StarrocksContainer starrocks = new StarrocksContainer(DockerImageNames.starrocks())
		.withDatabaseName("test")
		.withScriptPaths("init.sql");

	@BeforeAll
	static void beforeAll() {
		starrocks.start();
	}

	@AfterAll
	static void afterAll() {
		starrocks.stop();
	}

	@Test
	void test_sql() throws SQLException {
		Connection connection = starrocks.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT id, name FROM user");
		while (rs.next()) {
			Assertions.assertThat(rs.getLong(1)).isEqualTo(1L);
			Assertions.assertThat(rs.getString(2)).isEqualTo("laokou");
		}
		rs.close();
		statement.close();
	}

}
