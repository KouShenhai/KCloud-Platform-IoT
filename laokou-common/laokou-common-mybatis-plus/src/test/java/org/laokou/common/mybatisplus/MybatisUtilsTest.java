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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.util.DateUtils;
import org.laokou.common.mybatisplus.util.MybatisUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MybatisUtilsTest {

	private final TestUserMapper testUserMapper;

	private final MybatisUtils mybatisUtils;

	@Test
	void test() {
		Assertions.assertNotNull(testUserMapper);
		Assertions.assertNotNull(mybatisUtils);
		Assertions.assertDoesNotThrow(
				() -> mybatisUtils.batch(List.of(getTestUserDO()), TestUserMapper.class, TestUserMapper::insert));
		List<TestUserDO> list = testUserMapper.selectList(Wrappers.emptyWrapper());
		Assertions.assertEquals(2, list.size());
		Assertions.assertTrue(list.stream().map(TestUserDO::getName).anyMatch("张三"::equals));
		Assertions.assertDoesNotThrow(() -> testUserMapper.deleteUser(List.of(8L)));
		Assertions.assertEquals(1, testUserMapper.selectObjectCount(new PageQuery()));
		Assertions.assertDoesNotThrow(() -> mybatisUtils.batch(List.of(getTestUserDO()), TestUserMapper.class, "master",
				TestUserMapper::insert));
		Assertions.assertEquals(2, testUserMapper.selectObjectCount(new PageQuery()));
		Assertions.assertDoesNotThrow(() -> testUserMapper.deleteUser(List.of(8L)));
		Assertions.assertEquals(1, testUserMapper.selectObjectCount(new PageQuery()));
		Assertions.assertDoesNotThrow(() -> mybatisUtils.batch(List.of(getTestUserDO()), 1000, 100, 180,
				TestUserMapper.class, "master", TestUserMapper::insert));
		Assertions.assertEquals(2, testUserMapper.selectObjectCount(new PageQuery()));
		Assertions.assertDoesNotThrow(() -> testUserMapper.deleteUser(List.of(8L)));
		Assertions.assertEquals(1, testUserMapper.selectObjectCount(new PageQuery()));
		Assertions.assertDoesNotThrow(() -> mybatisUtils.batch(List.of(getTestUserDO()), 1000, 100, 180,
				TestUserMapper.class, TestUserMapper::insert));
		Assertions.assertEquals(2, testUserMapper.selectObjectCount(new PageQuery()));
		Assertions.assertDoesNotThrow(() -> testUserMapper.deleteUser(List.of(8L)));
		Assertions.assertEquals(1, testUserMapper.selectObjectCount(new PageQuery()));
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

}
