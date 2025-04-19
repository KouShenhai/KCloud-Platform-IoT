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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.mybatisplus.util.SqlUtils;

/**
 * @author laokou
 */
class SqlUtilsTest {

	@Test
	void test() {
		String sql = "select * from t_user \nwhere id = 1";
		PlainSelect plainSelect = SqlUtils.plainSelect(sql);
		Assertions.assertEquals("t_user", plainSelect.getFromItem().toString());
		Assertions.assertEquals("id = 1", plainSelect.getWhere().toString());
		Assertions.assertEquals("SELECT * FROM t_user WHERE id = 1", SqlUtils.formatSql(sql));
	}

}
