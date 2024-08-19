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

package org.laokou.api.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.secret.annotation.ApiSecret;
import org.laokou.common.sensitive.annotation.Sensitive;
import org.laokou.common.sensitive.annotation.SensitiveType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * @author laokou
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "ApisController", description = "API管理")
public class ApisController {

	@GetMapping("/test")
	public Test2 test() {
		return new Test2("2413176044@qq.com");
	}

	@ApiSecret
	@PostMapping("v3/apis")
	public void test(@RequestBody Test test) {
		log.info("{}", test);
	}

	@Data
	public static class Test implements Serializable {

		private String id;

		private String name;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Test2 {

		@Sensitive(type = SensitiveType.MAIL)
		private String mail;

	}

}
