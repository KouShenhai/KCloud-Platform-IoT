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

package org.laokou.common.i18n;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.LocaleUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author laokou
 */
class LocaleUtilsTest {

	@Test
	void test_toLocale_withValidLanguage() {
		assertLocale(LocaleUtils.toLocale("zh-CN"), "zh", "CN");
	}

	@Test
	void test_toLocale_withEnglishUS() {
		assertLocale(LocaleUtils.toLocale("en-US"), "en", "US");
	}

	@Test
	void test_toLocale_withMultipleLanguages() {
		assertLocale(LocaleUtils.toLocale("zh-CN,en-US,ja-JP"), "zh", "CN");
	}

	@Test
	void test_toLocale_withEmptyString() {
		assertDefaultLocale(LocaleUtils.toLocale(""));
	}

	@Test
	void test_toLocale_withNull() {
		assertDefaultLocale(LocaleUtils.toLocale(null));
	}

	@Test
	void test_toLocale_withInvalidFormat() {
		assertDefaultLocale(LocaleUtils.toLocale("invalid"));
	}

	@Test
	void test_toLocale_withOnlyLanguageCode() {
		assertDefaultLocale(LocaleUtils.toLocale("zh"));
	}

	@Test
	void test_toLocale_withMultipleLanguagesNoValidFormat() {
		assertDefaultLocale(LocaleUtils.toLocale("zh,en,ja"));
	}

	@Test
	void test_toLocale_withMixedValidInvalidLanguages() {
		assertLocale(LocaleUtils.toLocale("invalid,zh-CN,en-US"), "zh", "CN");
	}

	@Test
	void test_toLocale_withJapanese() {
		assertLocale(LocaleUtils.toLocale("ja-JP"), "ja", "JP");
	}

	@Test
	void test_toLocale_withFrench() {
		assertLocale(LocaleUtils.toLocale("fr-FR"), "fr", "FR");
	}

	@Test
	void test_toLocale_withGerman() {
		assertLocale(LocaleUtils.toLocale("de-DE"), "de", "DE");
	}

	@Test
	void test_toLocale_withSpanish() {
		assertLocale(LocaleUtils.toLocale("es-ES"), "es", "ES");
	}

	@Test
	void test_toLocale_withWhitespace() {
		assertDefaultLocale(LocaleUtils.toLocale("   "));
	}

	@Test
	void test_toLocale_withExtraHyphens() {
		assertDefaultLocale(LocaleUtils.toLocale("zh-CN-extra"));
	}

	@Test
	void test_toLocale_withAcceptLanguageHeader() {
		assertLocale(LocaleUtils.toLocale("zh-CN,zh;q=0.9,en;q=0.8"), "zh", "CN");
	}

	private void assertLocale(Locale locale, String expectedLanguage, String expectedCountry) {
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo(expectedLanguage);
		Assertions.assertThat(locale.getCountry()).isEqualTo(expectedCountry);
	}

	private void assertDefaultLocale(Locale locale) {
		Assertions.assertThat(locale).isNotNull().isEqualTo(LocaleContextHolder.getLocale());
	}

}
