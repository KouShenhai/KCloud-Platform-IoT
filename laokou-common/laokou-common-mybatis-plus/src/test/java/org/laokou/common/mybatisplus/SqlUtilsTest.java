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

import net.sf.jsqlparser.statement.select.PlainSelect;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.mybatisplus.util.SqlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
class SqlUtilsTest {

	@Test
	void test_formatSql_withValidSelectSql() {
		String sql = "SELECT * FROM user WHERE id = 1";
		String formattedSql = SqlUtils.formatSql(sql);
		Assertions.assertThat(formattedSql)
			.isNotNull()
			.isNotEmpty()
			.contains("SELECT")
			.contains("FROM")
			.contains("user");
	}

	@Test
	void test_formatSql_withValidInsertSql() {
		String sql = "INSERT INTO user (id, name) VALUES (1, 'test')";
		String formattedSql = SqlUtils.formatSql(sql);
		Assertions.assertThat(formattedSql)
			.isNotNull()
			.isNotEmpty()
			.contains("INSERT")
			.contains("INTO")
			.contains("user");
	}

	@Test
	void test_formatSql_withValidUpdateSql() {
		String sql = "UPDATE user SET name = 'test' WHERE id = 1";
		String formattedSql = SqlUtils.formatSql(sql);
		Assertions.assertThat(formattedSql)
			.isNotNull()
			.isNotEmpty()
			.contains("UPDATE")
			.contains("user")
			.contains("SET");
	}

	@Test
	void test_formatSql_withValidDeleteSql() {
		String sql = "DELETE FROM user WHERE id = 1";
		String formattedSql = SqlUtils.formatSql(sql);
		Assertions.assertThat(formattedSql)
			.isNotNull()
			.isNotEmpty()
			.contains("DELETE")
			.contains("FROM")
			.contains("user");
	}

	@Test
	void test_formatSql_withComplexSelectSql() {
		String sql = "SELECT u.id, u.name, d.dept_name FROM user u LEFT JOIN dept d ON u.dept_id = d.id WHERE u.id > 10 ORDER BY u.id DESC";
		String formattedSql = SqlUtils.formatSql(sql);
		Assertions.assertThat(formattedSql)
			.isNotNull()
			.isNotEmpty()
			.contains("SELECT")
			.contains("LEFT JOIN")
			.contains("ORDER BY");
	}

	@Test
	void test_formatSql_withInvalidSql() {
		String invalidSql = "INVALID SQL STATEMENT";
		Assertions.assertThatThrownBy(() -> SqlUtils.formatSql(invalidSql))
			.isInstanceOf(SystemException.class)
			.hasMessageContaining("SQL解析失败");
	}

	@Test
	void test_formatSql_withEmptySql() {
		String emptySql = "";
		Assertions.assertThatThrownBy(() -> SqlUtils.formatSql(emptySql))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("SQL cannot be empty");
	}

	@Test
	void test_formatSql_withNullSql() {
		Assertions.assertThatThrownBy(() -> SqlUtils.formatSql(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("SQL cannot be empty");
	}

	@Test
	void test_plainSelect_withValidSelectSql() {
		String sql = "SELECT id, name FROM user WHERE id = 1";
		PlainSelect plainSelect = SqlUtils.plainSelect(sql);
		Assertions.assertThat(plainSelect).isNotNull();
		Assertions.assertThat(plainSelect.getSelectItems()).isNotEmpty();
		Assertions.assertThat(plainSelect.getFromItem()).isNotNull();
	}

	@Test
	void test_plainSelect_withComplexSelectSql() {
		String sql = "SELECT u.id, u.name, d.dept_name FROM user u LEFT JOIN dept d ON u.dept_id = d.id WHERE u.id > 10";
		PlainSelect plainSelect = SqlUtils.plainSelect(sql);
		Assertions.assertThat(plainSelect).isNotNull();
		Assertions.assertThat(plainSelect.getSelectItems()).isNotEmpty();
		Assertions.assertThat(plainSelect.getJoins()).isNotEmpty();
		Assertions.assertThat(plainSelect.getWhere()).isNotNull();
	}

	@Test
	void test_plainSelect_withInvalidSql() {
		String invalidSql = "INVALID SQL";
		Assertions.assertThatThrownBy(() -> SqlUtils.plainSelect(invalidSql))
			.isInstanceOf(SystemException.class)
			.hasMessageContaining("SQL解析失败");
	}

	@Test
	void test_plainSelect_withNonSelectSql() {
		String insertSql = "INSERT INTO user (id, name) VALUES (1, 'test')";
		Assertions.assertThatThrownBy(() -> SqlUtils.plainSelect(insertSql)).isInstanceOf(ClassCastException.class);
	}

	@Test
	void test_getCompleteSql_withNoParameters() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE id = 1";
		BoundSql boundSql = new BoundSql(configuration, sql, new ArrayList<>(), null);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo(sql);
	}

	@Test
	void test_getCompleteSql_withMultipleWhitespace() {
		Configuration configuration = new Configuration();
		String sql = "SELECT   *   FROM   user   WHERE   id   =   1";
		BoundSql boundSql = new BoundSql(configuration, sql, new ArrayList<>(), null);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE id = 1");
	}

	@Test
	void test_getCompleteSql_withStringParameter() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE name = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "name", String.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("name", "张三");
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE name = '张三'");
	}

	@Test
	void test_getCompleteSql_withIntegerParameter() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE id = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "id", Integer.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("id", 123);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE id = 123");
	}

	@Test
	void test_getCompleteSql_withLongParameter() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE id = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "id", Long.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("id", 999999999L);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE id = 999999999");
	}

	@Test
	void test_getCompleteSql_withNullParameter() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE name = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "name", String.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("name", null);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE name = NULL");
	}

	@Test
	void test_getCompleteSql_withMultipleParameters() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE id = ? AND name = ? AND age = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "id", Integer.class).build());
		parameterMappings.add(new ParameterMapping.Builder(configuration, "name", String.class).build());
		parameterMappings.add(new ParameterMapping.Builder(configuration, "age", Integer.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("id", 1);
		parameterObject.put("name", "李四");
		parameterObject.put("age", 25);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE id = 1 AND name = '李四' AND age = 25");
	}

	@Test
	void test_getCompleteSql_withStringContainingSingleQuote() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE name = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "name", String.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("name", "O'Brien");
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE name = 'O''Brien'");
	}

	@Test
	void test_getCompleteSql_withBooleanParameter() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE active = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "active", Boolean.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("active", true);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE active = true");
	}

	@Test
	void test_getCompleteSql_withDoubleParameter() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM product WHERE price = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "price", Double.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("price", 99.99);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM product WHERE price = 99.99");
	}

	@Test
	void test_getCompleteSql_withAdditionalParameter() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE id = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "id", Integer.class).build());
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, null);
		boundSql.setAdditionalParameter("id", 100);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE id = 100");
	}

	@Test
	void test_getCompleteSql_withNullParameterObject() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE id = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "id", Integer.class).build());
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, null);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE id = NULL");
	}

	@Test
	void test_getCompleteSql_withSimpleTypeParameter() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE id = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "id", Integer.class).build());
		Integer parameterObject = 888;
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE id = 888");
	}

	@Test
	void test_getCompleteSql_withEmptyString() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE name = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "name", String.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("name", "");
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE name = ''");
	}

	@Test
	void test_getCompleteSql_withChineseCharacters() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE name = ? AND address = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "name", String.class).build());
		parameterMappings.add(new ParameterMapping.Builder(configuration, "address", String.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("name", "王二");
		parameterObject.put("address", "北京市朝阳区");
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE name = '王二' AND address = '北京市朝阳区'");
	}

	@Test
	void test_getCompleteSql_withSpecialCharacters() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE email = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "email", String.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("email", "test@example.com");
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE email = 'test@example.com'");
	}

	@Test
	void test_getCompleteSql_withMixedParameters() {
		Configuration configuration = new Configuration();
		String sql = "INSERT INTO user (id, name, age, active, salary) VALUES (?, ?, ?, ?, ?)";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "id", Long.class).build());
		parameterMappings.add(new ParameterMapping.Builder(configuration, "name", String.class).build());
		parameterMappings.add(new ParameterMapping.Builder(configuration, "age", Integer.class).build());
		parameterMappings.add(new ParameterMapping.Builder(configuration, "active", Boolean.class).build());
		parameterMappings.add(new ParameterMapping.Builder(configuration, "salary", Double.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("id", 10L);
		parameterObject.put("name", "赵六");
		parameterObject.put("age", 30);
		parameterObject.put("active", false);
		parameterObject.put("salary", 8888.88);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql)
			.isEqualTo("INSERT INTO user (id, name, age, active, salary) VALUES (10, '赵六', 30, false, 8888.88)");
	}

	@Test
	void test_getCompleteSql_withConsecutiveQuestionMarks() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE id = ? AND name = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "id", Integer.class).build());
		parameterMappings.add(new ParameterMapping.Builder(configuration, "name", String.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("id", 5);
		parameterObject.put("name", "测试");
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE id = 5 AND name = '测试'");
	}

	@Test
	void test_getCompleteSql_withZeroValue() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM user WHERE age = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "age", Integer.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("age", 0);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM user WHERE age = 0");
	}

	@Test
	void test_getCompleteSql_withNegativeValue() {
		Configuration configuration = new Configuration();
		String sql = "SELECT * FROM account WHERE balance = ?";
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		parameterMappings.add(new ParameterMapping.Builder(configuration, "balance", Double.class).build());
		Map<String, Object> parameterObject = new HashMap<>();
		parameterObject.put("balance", -100.50);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		String completeSql = SqlUtils.getCompleteSql(boundSql);
		Assertions.assertThat(completeSql).isEqualTo("SELECT * FROM account WHERE balance = -100.5");
	}

}
