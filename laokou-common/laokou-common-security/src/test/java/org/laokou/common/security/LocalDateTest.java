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

package org.laokou.common.security;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.DateUtil;

import java.time.LocalDate;

/**
 * @author laokou
 */
@Slf4j
public class LocalDateTest {

	public static void main(String[] args) {
		LocalDate localDate = LocalDate.of(2023, 2, 28);
		log.info("{}", DateUtil.format(DateUtil.plusMonths(localDate, 1), DateUtil.YYYYMM));
	}

}
