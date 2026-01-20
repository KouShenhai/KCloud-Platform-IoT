/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
	void test_toLocale_withAcceptLanguageHeader() {
		assertLocale(LocaleUtils.toLocale("zh-CN,zh;q=0.9,en;q=0.8"), "zh", "CN");
	}

	@Test
	void test_toLocale_withNullLanguage() {
		// Should return default locale from LocaleContextHolder
		Locale locale = LocaleUtils.toLocale(null);
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	void test_toLocale_withEmptyString() {
		// Should return default locale from LocaleContextHolder
		Locale locale = LocaleUtils.toLocale("");
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	void test_toLocale_withBlankString() {
		// Should return default locale from LocaleContextHolder
		Locale locale = LocaleUtils.toLocale("   ");
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	void test_toLocale_withInvalidFormat() {
		// No hyphen, should return default locale
		Locale locale = LocaleUtils.toLocale("invalid");
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	void test_toLocale_withOnlyLanguageCode() {
		// Only language code without country, should return default locale
		Locale locale = LocaleUtils.toLocale("zh");
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	void test_toLocale_withMultipleCommasNoValid() {
		// Multiple commas but no valid language-country pair
		Locale locale = LocaleUtils.toLocale("invalid,another,test");
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	void test_toLocale_withKorean() {
		assertLocale(LocaleUtils.toLocale("ko-KR"), "ko", "KR");
	}

	@Test
	void test_toLocale_withPortuguese() {
		assertLocale(LocaleUtils.toLocale("pt-BR"), "pt", "BR");
	}

	@Test
	void test_toLocale_withItalian() {
		assertLocale(LocaleUtils.toLocale("it-IT"), "it", "IT");
	}

	@Test
	void test_toLocale_withRussian() {
		assertLocale(LocaleUtils.toLocale("ru-RU"), "ru", "RU");
	}

	@Test
	void test_toLocale_withEnglishGB() {
		assertLocale(LocaleUtils.toLocale("en-GB"), "en", "GB");
	}

	@Test
	void test_toLocale_withTraditionalChinese() {
		assertLocale(LocaleUtils.toLocale("zh-TW"), "zh", "TW");
	}

	@Test
	void test_toLocale_withComplexAcceptHeader() {
		// Complex Accept-Language header with quality values
		assertLocale(LocaleUtils.toLocale("en-US;q=0.8,zh-CN;q=0.9,ja-JP;q=0.7"), "en", "US");
	}

	@Test
	void test_toLocale_withLeadingComma() {
		assertLocale(LocaleUtils.toLocale(",zh-CN"), "zh", "CN");
	}

	@Test
	void test_toLocale_withTrailingComma() {
		assertLocale(LocaleUtils.toLocale("zh-CN,"), "zh", "CN");
	}

	@Test
	void test_toLocale_withSpacesAroundLanguage() {
		// Spaces might cause parsing issues, should handle gracefully
		Locale locale = LocaleUtils.toLocale(" zh-CN ");
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	void test_toLocale_withLowercaseCountryCode() {
		// Lowercase country code should work
		assertLocale(LocaleUtils.toLocale("zh-cn"), "zh", "CN");
	}

	@Test
	void test_toLocale_withUppercaseLanguageCode() {
		// Uppercase language code should work
		assertLocale(LocaleUtils.toLocale("ZH-CN"), "zh", "CN");
	}

	@Test
	void test_toLocale_withOnlyHyphen() {
		// Only hyphen, should return default locale
		Locale locale = LocaleUtils.toLocale("-");
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	void test_toLocale_withMultipleHyphens() {
		// Multiple hyphens, should parse first two parts
		Locale locale = LocaleUtils.toLocale("zh-CN-Hans");
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("zh");
		Assertions.assertThat(locale.getCountry()).isEqualTo("CN");
	}

	private void assertLocale(Locale locale, String expectedLanguage, String expectedCountry) {
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo(expectedLanguage);
		Assertions.assertThat(locale.getCountry()).isEqualTo(expectedCountry);
	}

}
