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
import org.junit.jupiter.api.Test;
import org.laokou.common.mybatisplus.util.SqlUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class SqlUtilsTest {

	@Test
	void test() {
		String sql = "select * from t_user \nwhere id = 1";
		PlainSelect plainSelect = SqlUtils.plainSelect(sql);
		assertThat(plainSelect.getFromItem().toString()).isEqualTo("t_user");
		assertThat(plainSelect.getWhere().toString()).isEqualTo("id = 1");
		assertThat(SqlUtils.formatSql(sql)).isEqualTo("SELECT * FROM t_user WHERE id = 1");
	}

}
