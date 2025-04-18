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

import org.laokou.common.excel.util.ExcelUtils;
import org.laokou.common.i18n.util.DateUtils;

import java.util.List;

/**
 * @author laokou
 */
final class TestUserConvertor implements ExcelUtils.ExcelConvertor<TestUserDO, TestUserExcel> {

	public static final TestUserConvertor INSTANCE = new TestUserConvertor();

	private TestUserConvertor() {
	}

	@Override
	public List<TestUserExcel> toExcels(List<TestUserDO> list) {
		return list.stream().map(TestUserConvertor::toExcel).toList();
	}

	@Override
	public TestUserDO toDataObject(TestUserExcel testUserExcel) {
		TestUserDO testUserDO = new TestUserDO();
		testUserDO.setId(testUserExcel.getId());
		testUserDO.setName(testUserExcel.getName());
		testUserDO.setCreator(1L);
		testUserDO.setEditor(1L);
		testUserDO.setCreateTime(DateUtils.nowInstant());
		testUserDO.setUpdateTime(DateUtils.nowInstant());
		testUserDO.setVersion(0);
		testUserDO.setTenantId(0L);
		testUserDO.setDelFlag(0);
		return testUserDO;
	}

	private static TestUserExcel toExcel(TestUserDO testUserDO) {
		TestUserExcel testUserExcel = new TestUserExcel();
		testUserExcel.setId(testUserDO.getId());
		testUserExcel.setName(testUserDO.getName());
		return testUserExcel;
	}

}
