/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.generator.repository.TableColumnDO;
import org.laokou.generator.repository.TableMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class TableTest {

	private final TableMapper tableMapper;

	@Test
	void testTable() {
		Map<String, String> tables = tableMapper.selectTables(Collections.emptySet());
		log.info("获取表：{}", JacksonUtil.toJsonStr(tables));
	}

	@Test
	void testTableColumn() {
		List<TableColumnDO> tableColumns = tableMapper.selectTableColumns(Collections.emptySet());
		log.info("获取字段：{}", JacksonUtil.toJsonStr(tableColumns));
	}

}
