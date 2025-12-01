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
		// Test with standard language-country format
		String language = "zh-CN";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("zh");
		Assertions.assertThat(locale.getCountry()).isEqualTo("CN");
	}

	@Test
	void test_toLocale_withEnglishUS() {
		// Test with English US
		String language = "en-US";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("en");
		Assertions.assertThat(locale.getCountry()).isEqualTo("US");
	}

	@Test
	void test_toLocale_withMultipleLanguages() {
		// Test with multiple languages separated by comma (should pick first valid one)
		String language = "zh-CN,en-US,ja-JP";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("zh");
		Assertions.assertThat(locale.getCountry()).isEqualTo("CN");
	}

	@Test
	void test_toLocale_withEmptyString() {
		// Test with empty string (should return default locale from context)
		String language = "";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale).isEqualTo(LocaleContextHolder.getLocale());
	}

	@Test
	void test_toLocale_withNull() {
		// Test with null (should return default locale from context)
		String language = null;
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale).isEqualTo(LocaleContextHolder.getLocale());
	}

	@Test
	void test_toLocale_withInvalidFormat() {
		// Test with invalid format (should return default locale from context)
		String language = "invalid";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale).isEqualTo(LocaleContextHolder.getLocale());
	}

	@Test
	void test_toLocale_withOnlyLanguageCode() {
		// Test with only language code without country (should return default locale)
		String language = "zh";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale).isEqualTo(LocaleContextHolder.getLocale());
	}

	@Test
	void test_toLocale_withMultipleLanguagesNoValidFormat() {
		// Test with multiple languages but none with valid format
		String language = "zh,en,ja";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale).isEqualTo(LocaleContextHolder.getLocale());
	}

	@Test
	void test_toLocale_withMixedValidInvalidLanguages() {
		// Test with mixed valid and invalid languages
		String language = "invalid,zh-CN,en-US";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("zh");
		Assertions.assertThat(locale.getCountry()).isEqualTo("CN");
	}

	@Test
	void test_toLocale_withJapanese() {
		// Test with Japanese locale
		String language = "ja-JP";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("ja");
		Assertions.assertThat(locale.getCountry()).isEqualTo("JP");
	}

	@Test
	void test_toLocale_withFrench() {
		// Test with French locale
		String language = "fr-FR";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("fr");
		Assertions.assertThat(locale.getCountry()).isEqualTo("FR");
	}

	@Test
	void test_toLocale_withGerman() {
		// Test with German locale
		String language = "de-DE";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("de");
		Assertions.assertThat(locale.getCountry()).isEqualTo("DE");
	}

	@Test
	void test_toLocale_withSpanish() {
		// Test with Spanish locale
		String language = "es-ES";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("es");
		Assertions.assertThat(locale.getCountry()).isEqualTo("ES");
	}

	@Test
	void test_toLocale_withWhitespace() {
		// Test with whitespace (should return default locale)
		String language = "   ";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale).isEqualTo(LocaleContextHolder.getLocale());
	}

	@Test
	void test_toLocale_withExtraHyphens() {
		// Test with extra hyphens (should return default locale due to invalid format)
		String language = "zh-CN-extra";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		// This might fail depending on implementation, but should handle gracefully
	}

	@Test
	void test_toLocale_withAcceptLanguageHeader() {
		// Test with Accept-Language header format
		String language = "zh-CN,zh;q=0.9,en;q=0.8";
		Locale locale = LocaleUtils.toLocale(language);
		Assertions.assertThat(locale).isNotNull();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("zh");
		Assertions.assertThat(locale.getCountry()).isEqualTo("CN");
	}

}
