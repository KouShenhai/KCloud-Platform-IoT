/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.test.mybatisplus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.test.mybatisplus.entity.T1;
import org.laokou.test.mybatisplus.entity.T2;
import org.laokou.test.mybatisplus.mapper.T1Mapper;
import org.laokou.test.mybatisplus.mapper.T2Mapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@SpringBootTest
@Slf4j
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class MybatisPlusTest {

	private final T1Mapper t1Mapper;

	private final T2Mapper t2Mapper;

	// @Test
	public void test() throws IOException {
		String path = "xxx\\1_sql.txt";
		File file = new File(path);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = bufferedReader.readLine();
		List<T1> list = new ArrayList<>(5000);
		while ((line = bufferedReader.readLine()) != null) {
			String[] split = line.split("\t");
			if (split.length > 1) {
				String str = split[1];
				if ("NULL".equals(str)) {
					continue;
				}
				if (!RegexUtil.mobileRegex(str)) {
					continue;
				}
				list.add(new T1(str));
				if (list.size() % 5000 == 0) {
					t1Mapper.insertBatch(list);
					list.clear();
				}
			}
		}
		if (list.size() > 0) {
			t1Mapper.insertBatch(list);
		}
	}

	// @Test
	public void test1() throws IOException {
		String path = "xxx\\2_sql.txt";
		File file = new File(path);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		List<T2> list = new ArrayList<>(5000);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			String[] split = line.split(",");
			if (split.length > 67) {
				String str = split[67].replace("C-", "");
				if (!RegexUtil.mobileRegex(str)) {
					continue;
				}
				if ("NULL".equals(str)) {
					continue;
				}
				list.add(new T2(str));
				if (list.size() % 5000 == 0) {
					t2Mapper.insertBatch(list);
					list.clear();
				}
			}
		}
		if (list.size() > 0) {
			t2Mapper.insertBatch(list);
		}
	}

}
