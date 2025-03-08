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

package org.laokou.common.ruleengine.api;

import java.util.Objects;

/**
 * @author cola
 */
@FunctionalInterface
public interface Condition {

	/**
	 * A NoOp {@link Condition} that always returns false.
	 */
	Condition FALSE = facts -> false;

	/**
	 * A NoOp {@link Condition} that always returns true.
	 */
	Condition TRUE = facts -> true;

	boolean evaluate(Facts facts);

	// 谓词and逻辑，参考Predicate
	default Condition and(Condition other) {
		Objects.requireNonNull(other);
		return (facts) -> this.evaluate(facts) && other.evaluate(facts);
	}

	// 谓词or逻辑，参考Predicate
	default Condition or(Condition other) {
		Objects.requireNonNull(other);
		return (facts) -> this.evaluate(facts) || other.evaluate(facts);
	}

}
