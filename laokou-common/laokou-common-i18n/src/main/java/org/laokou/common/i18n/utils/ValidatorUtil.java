/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.i18n.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.laokou.common.i18n.common.GlobalException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * <a href=
 * "https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-validating-bean-constraints">...</a>
 *
 * @author laokou
 */
public class ValidatorUtil {

	private static final ReloadableResourceBundleMessageSource VALIDATE_BUNDLE_MESSAGE_SOURCE;

	static {
		VALIDATE_BUNDLE_MESSAGE_SOURCE = new ReloadableResourceBundleMessageSource();
		VALIDATE_BUNDLE_MESSAGE_SOURCE.setDefaultEncoding(StandardCharsets.UTF_8.name());
		VALIDATE_BUNDLE_MESSAGE_SOURCE.setBasename("classpath:i18n/validation");
	}

	/**
	 * 校验对象
	 * @param obj 待校验对象
	 */
	public static void validateEntity(Object obj) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Object>> violationSet = validator.validate(obj);
		if (!violationSet.isEmpty()) {
			String message = violationSet.iterator().next().getMessage();
			throw new GlobalException(message);
		}
	}

}
