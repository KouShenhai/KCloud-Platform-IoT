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
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.FileUtils;
import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.util.ResourceUtils;
import org.laokou.common.mybatisplus.util.MybatisUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ExcelTest {

	private final TestUserMapper testUserMapper;

	private final MybatisUtils mybatisUtils;

	@Test
	void testExport() {
		assertThat(testUserMapper).isNotNull();
		List<TestUserDO> list = testUserMapper.selectList(Wrappers.emptyWrapper());
		assertThat(list.size()).isEqualTo(1);
		assertThat(list.stream().map(TestUserDO::getName).toList().contains("老寇")).isTrue();
		assertThatNoException().isThrownBy(() -> ExcelUtils.doImport(EMPTY, TestUserExcel.class,
				TestUserConvertor.INSTANCE, ResourceUtils.getResource("classpath:test3.xlsx").getInputStream(),
				TestUserMapper.class, TestUserMapper::insert, mybatisUtils));
		assertThatNoException().isThrownBy(() -> ExcelUtils.doImport("test", TestUserExcel.class,
				TestUserConvertor.INSTANCE, ResourceUtils.getResource("classpath:test2.xlsx").getInputStream(),
				TestUserMapper.class, TestUserMapper::insert, mybatisUtils));
		assertThatNoException().isThrownBy(() -> ExcelUtils.doImport("test2", TestUserExcel.class,
				TestUserConvertor.INSTANCE, ResourceUtils.getResource("classpath:test2.xlsx").getInputStream(),
				TestUserMapper.class, TestUserMapper::insert, mybatisUtils));
		assertThatNoException().isThrownBy(() -> ExcelUtils.doImport("test3", TestUserExcel.class,
				TestUserConvertor.INSTANCE, ResourceUtils.getResource("classpath:test2.xlsx").getInputStream(),
				TestUserMapper.class, TestUserMapper::insert, mybatisUtils));
		long count = testUserMapper.selectObjectCount(new PageQuery());
		assertThat(count).isEqualTo(7);
		assertThatNoException()
			.isThrownBy(() -> ExcelUtils.doExport("测试用户Sheet页", 1000, new FileOutputStream("test.xlsx"),
					new PageQuery(), testUserMapper, TestUserExcel.class, TestUserConvertor.INSTANCE));
		assertThatNoException().isThrownBy(() -> FileUtils.deleteIfExists(Path.of("test.xlsx")));
		assertThatNoException().isThrownBy(() -> testUserMapper.deleteUser(List.of(2L, 3L, 4L, 5L, 6L, 7L)));
		count = testUserMapper.selectObjectCount(new PageQuery());
		assertThat(count).isEqualTo(1);
	}

}
