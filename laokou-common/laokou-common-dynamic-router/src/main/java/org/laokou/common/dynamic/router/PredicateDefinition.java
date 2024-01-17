/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.dynamic.router;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.laokou.common.dynamic.router.utils.NameUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.validation.annotation.Validated;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * 谓语定义配置.
 *
 * @author Spencer Gibb
 */
@Data
@Validated
public class PredicateDefinition {

	@NotNull
	private String name;

	private Map<String, String> args = new LinkedHashMap<>();

	public PredicateDefinition() {
	}

	public PredicateDefinition(String text) {
		int eqIdx = text.indexOf('=');
		if (eqIdx <= 0) {
			throw new ValidationException(
					"Unable to parse PredicateDefinition text '" + text + "'" + ", must be of the form name=value");
		}
		setName(text.substring(0, eqIdx));
		String[] args = tokenizeToStringArray(text.substring(eqIdx + 1), ",");
		for (int i = 0; i < args.length; i++) {
			this.args.put(NameUtil.generateName(i), args[i]);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (ObjectUtil.isNull(o) || getClass() != o.getClass()) {
			return false;
		}
		PredicateDefinition that = (PredicateDefinition) o;
		return ObjectUtil.equals(name, that.name) && ObjectUtil.equals(args, that.args);
	}

	@Override
	public int hashCode() {
		return ObjectUtil.hash(name, args);
	}

	@Override
	public String toString() {
		return "PredicateDefinition{" + "name='" + name + '\'' + ", args=" + args + '}';
	}

}
