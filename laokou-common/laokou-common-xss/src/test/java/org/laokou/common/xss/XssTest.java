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

package org.laokou.common.xss;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.xss.util.XssUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
@Slf4j
class XssTest {

	private final String[] xssAttackVectors = { "<script>alert(1)</script>", "<IMG SRC=\"javascript:alert('XSS');\">",
			"<svg/onload=alert(1)>",
			"javascript:/*-/*`/*\\`/*'/*\"/**/(/* */onerror=alert(1) )//%0D%0A%0d%0a//</stYle/</titLe/</teXtarEa/</scRipt/--!>\\x3csVg/<sVg/oNloAd=alert(1)//>\\x3e",
			"select 1 from test" };

	@Test
	void test_htmlScripTagJsonString() {
		String json = "{\"s\": \"" + xssAttackVectors[0] + "\"}";
		String cleaned = XssUtils.clearHtml(json);
		assertThat(cleaned).isEqualTo("{\"s\": \"alert(1)\"}");
		assertThat(cleaned.startsWith("{") && cleaned.endsWith("}")).isTrue();
		assertThat(cleaned.contains("<script>")).isFalse();
	}

	@Test
	void test_htmlTagJsonString() {
		String json = "{\"s\": \"" + xssAttackVectors[1] + "\"}";
		String cleaned = XssUtils.clearHtml(json);
		assertThat(cleaned).isEqualTo("{\"s\": \"<img>\"}");
		assertThat(cleaned.startsWith("{") && cleaned.endsWith("}")).isTrue();
		assertThat(cleaned.contains("<IMG")).isFalse();
	}

	@Test
	void test_svgJsonString() {
		String json = "{\"s\": \"" + xssAttackVectors[2] + "\"}";
		String cleaned = XssUtils.clearHtml(json);
		assertThat(cleaned).isEqualTo("{\"s\": \"\"}");
		assertThat(cleaned.startsWith("{") && cleaned.endsWith("}")).isTrue();
		assertThat(cleaned.contains("<svg")).isFalse();
	}

	@Test
	void test_htmlScriptJsonString() {
		String json = "{\"s\": \"" + xssAttackVectors[3] + "\"}";
		String cleaned = XssUtils.clearHtml(json);
		assertThat(cleaned)
			.isEqualTo("{\"s\": \"/*-/*`/*\\`/*'/*\"/**/(/* */onerror=alert(1) )//%0D%0A%0d%0a//\\x3csVg/\\x3e\"}");
		assertThat(cleaned.startsWith("{") && cleaned.endsWith("}")).isTrue();
		assertThat(cleaned.contains("javascript:")).isFalse();
	}

	@Test
	void test_sqlJsonString() {
		String json = xssAttackVectors[4];
		boolean sqlInjection = false;
		try {
			XssUtils.clearSql(json);
		}
		catch (Exception e) {
			sqlInjection = true;
		}
		assertThat(sqlInjection).isTrue();
	}

}
