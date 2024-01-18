/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.dynamic.router;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.*;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * 路由定义配置.
 *
 * @author Spencer Gibb
 */
@Data
@Validated
public class RouteDefinition {

	private String id;

	@NotEmpty
	@Valid
	private List<PredicateDefinition> predicates = new ArrayList<>();

	@Valid
	private List<FilterDefinition> filters = new ArrayList<>();

	@NotNull
	private URI uri;

	private Map<String, Object> metadata = new HashMap<>();

	private int order = 0;

	public RouteDefinition() {
	}

	public RouteDefinition(String text) {
		int eqIdx = text.indexOf('=');
		if (eqIdx <= 0) {
			throw new ValidationException(
					"Unable to parse RouteDefinition text '" + text + "'" + ", must be of the form name=value");
		}
		setId(text.substring(0, eqIdx));
		String[] args = tokenizeToStringArray(text.substring(eqIdx + 1), ",");
		setUri(URI.create(args[0]));
		for (int i = 1; i < args.length; i++) {
			this.predicates.add(new PredicateDefinition(args[i]));
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
		RouteDefinition that = (RouteDefinition) o;
		return this.order == that.order && ObjectUtil.equals(this.id, that.id)
				&& ObjectUtil.equals(this.predicates, that.predicates) && ObjectUtil.equals(this.filters, that.filters)
				&& ObjectUtil.equals(this.uri, that.uri) && ObjectUtil.equals(this.metadata, that.metadata);
	}

	@Override
	public int hashCode() {
		return ObjectUtil.hash(this.id, this.predicates, this.filters, this.uri, this.metadata, this.order);
	}

	@Override
	public String toString() {
		return "RouteDefinition{" + "id='" + id + '\'' + ", predicates=" + predicates + ", filters=" + filters
				+ ", uri=" + uri + ", order=" + order + ", metadata=" + metadata + '}';
	}

}
