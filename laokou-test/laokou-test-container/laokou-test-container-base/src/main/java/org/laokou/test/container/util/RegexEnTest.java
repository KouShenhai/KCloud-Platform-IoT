/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.test.container.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class RegexEnTest {

	public static void main(String[] args) {
		String reg = "^[A-Za-z]+$";
		String str = "111";
		String str2 = "aaZZ";
		String str3 = "ZZ111SSss";
		log.info("{}", Pattern.matches(reg, str));
		log.info("{}", Pattern.matches(reg, str2));
		log.info("{}", Pattern.matches(reg, str3));
	}

}
