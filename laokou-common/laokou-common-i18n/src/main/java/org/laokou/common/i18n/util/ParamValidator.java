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

package org.laokou.common.i18n.util;

import org.laokou.common.i18n.common.exception.ParamException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.laokou.common.i18n.common.constant.StringConstants.DROP;
import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;

/**
 * @author laokou
 */
public final class ParamValidator {

	private ParamValidator() {
	}

	public static void validate(Validate... validates) {
		String validateString = StringUtils.collectionToDelimitedString(validates(validates), DROP);
		if (StringUtils.isNotEmpty(validateString)) {
			throw new ParamException("P_System_ValidateFailed", validateString);
		}
	}

	public static Set<String> validates(Validate... validates) {
		return Stream.of(validates)
			.filter(item -> StringUtils.isNotEmpty(item.value))
			.map(item -> item.value)
			.collect(Collectors.toSet());
	}

	public static Validate validate() {
		return new Validate();
	}

	public static Validate invalidate(String value) {
		return new Validate(value);
	}

	public static class Validate {

		private final String value;

		public Validate() {
			this(EMPTY);
		}

		public Validate(String value) {
			this.value = value;
		}

	}

}
