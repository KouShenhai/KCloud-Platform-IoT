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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.i18n.dto.PageQuery;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ExcelTest {

	private final TestUserMapper testUserMapper;

	private final ExecutorService virtualThreadExecutor;

	@Test
	void testExport() throws IOException, InterruptedException {
		Assertions.assertNotNull(testUserMapper);
		List<TestUserDO> list = testUserMapper.selectList(Wrappers.emptyWrapper());
		Assertions.assertFalse(list.isEmpty());
		Assertions.assertTrue(list.stream().map(TestUserDO::getName).toList().contains("老寇"));
		ExcelUtils.doExport("测试用户Sheet页", 1000, new FileOutputStream("test.xlsx"), new PageQuery(), testUserMapper,
				TestUserExcel.class, TestUserConvertor.INSTANCE, virtualThreadExecutor);
		Thread.sleep(1000);
		FileUtils.forceDeleteOnExit(new File("test.xlsx"));
	}

}
