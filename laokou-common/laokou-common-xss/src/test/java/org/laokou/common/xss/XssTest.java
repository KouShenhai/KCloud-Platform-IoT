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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.xss.util.XssUtil;

/**
 * @author laokou
 */
@Slf4j
class XssTest {

	private static final String[] XSS_ATTACK_VECTORS = { "<script>alert(1)</script>",
			"<IMG SRC=\"javascript:alert('XSS');\">", "<svg/onload=alert(1)>",
			"javascript:/*-/*`/*\\`/*'/*\"/**/(/* */onerror=alert(1) )//%0D%0A%0d%0a//</stYle/</titLe/</teXtarEa/</scRipt/--!>\\x3csVg/<sVg/oNloAd=alert(1)//>\\x3e",
			"select 1 from test" };

	@Test
	void testHtmlScripTagJsonString() {
		String json = "{\"s\": \"" + XSS_ATTACK_VECTORS[0] + "\"}";
		String cleaned = XssUtil.clearHtml(json);
		Assertions.assertEquals("{\"s\": \"alert(1)\"}", cleaned);
		Assertions.assertTrue(cleaned.startsWith("{") && cleaned.endsWith("}"));
		Assertions.assertFalse(cleaned.contains("<script>"));
	}

	@Test
	void testHtmlTagJsonString() {
		String json = "{\"s\": \"" + XSS_ATTACK_VECTORS[1] + "\"}";
		String cleaned = XssUtil.clearHtml(json);
		Assertions.assertEquals("{\"s\": \"<img>\"}", cleaned);
		Assertions.assertTrue(cleaned.startsWith("{") && cleaned.endsWith("}"));
		Assertions.assertFalse(cleaned.contains("<IMG"));
	}

	@Test
	void testSvgJsonString() {
		String json = "{\"s\": \"" + XSS_ATTACK_VECTORS[2] + "\"}";
		String cleaned = XssUtil.clearHtml(json);
		Assertions.assertEquals("{\"s\": \"\"}", cleaned);
		Assertions.assertTrue(cleaned.startsWith("{") && cleaned.endsWith("}"));
		Assertions.assertFalse(cleaned.contains("<svg"));
	}

	@Test
	void testHtmlScriptJsonString() {
		String json = "{\"s\": \"" + XSS_ATTACK_VECTORS[3] + "\"}";
		String cleaned = XssUtil.clearHtml(json);
		Assertions.assertEquals(
				"{\"s\": \"/*-/*`/*\\`/*'/*\"/**/(/* */onerror=alert(1) )//%0D%0A%0d%0a//\\x3csVg/\\x3e\"}", cleaned);
		Assertions.assertTrue(cleaned.startsWith("{") && cleaned.endsWith("}"));
		Assertions.assertFalse(cleaned.contains("javascript:"));
	}

	@Test
	void testSqlJsonString() {
		String json = XSS_ATTACK_VECTORS[4];
		boolean sqlInjection = false;
		try {
			XssUtil.clearSql(json);
		}
		catch (Exception e) {
			sqlInjection = true;
		}
		Assertions.assertTrue(sqlInjection);
	}

}
