/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.crypto;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author laokou
 */
@Slf4j
public class DeptTreeMaxPathTest {

	public static void main(String[] args) {
		String str1 = "0,1535887940687765505,1535858679453085698";
		String str2 = "0,1535887940687765505,1535881356595175426";
		String str3 = "0,1535887940687765505,1535887129341599746";
		String str4 = "0,1535887940687765505";
		String str10 = "0,1535887940687765504";
		String str5 = "0,1535887940687765505,1535881356595175426,1584488175088373761";
		String str6 = "0,1535887940687765505,1535881356595175426,1584488411756171265";
		String str7 = "0,1535887940687765505,1535881356595175426,1584488175088373761,1584488411756171266";
		String str8 = "0,1535887940687765505,1535881356595175426,1584488175088373761,1584488411756171268";
		String str9 = "0,1535887940687765505,1535881356595175426,1584488175088373761,1584488411756171269";
		List<String> list = new ArrayList<>();
		list.add(str9);
		// list.add(str4);
		list.add(str5);
		list.add(str1);
		list.add(str8);
		list.add(str9);
		list.add(str10);
		// 字符串长度排序
		list.sort(Comparator.comparingInt(String::length));
		log.info("{}", String.join("   ", list));
		Set<String> paths = new HashSet<>(list.size());
		paths.add(list.getFirst());
		for (String p : list.subList(1, list.size())) {
			int find = paths.size();
			for (String path : paths) {
				if (p.contains(path)) {
					break;
				}
				find--;
			}
			if (find == 0) {
				paths.add(p);
			}
		}
		log.info("{}", paths);
	}

}
