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

package org.laokou.common.ruleengine.api;

import java.util.Objects;

/**
 * @author cola
 */
public class Fact<T> {

	private final String name;

	private final T value;

	/**
	 * Create a new fact.
	 * @param name of the fact
	 * @param value of the fact
	 */
	public Fact(String name, T value) {
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(value, "value must not be null");
		this.name = name;
		this.value = value;
	}

	/**
	 * Get the fact name.
	 * @return fact name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the fact value.
	 * @return fact value
	 */
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Fact{" + "name='" + name + '\'' + ", value=" + value + '}';
	}

	/*
	 * The Facts API represents a namespace for facts where each fact has a unique name.
	 * Hence, equals/hashcode are deliberately calculated only on the fact name.
	 */

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Fact<?> fact = (Fact<?>) o;
		return name.equals(fact.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

}
