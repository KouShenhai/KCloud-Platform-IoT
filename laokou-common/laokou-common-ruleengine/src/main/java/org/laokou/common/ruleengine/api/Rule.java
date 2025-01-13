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

/**
 * @author cola
 */
public interface Rule extends Comparable<Rule> {

	/**
	 * Default rule name.
	 */
	String DEFAULT_NAME = "rule";

	/**
	 * Default rule description.
	 */
	String DEFAULT_DESCRIPTION = "description";

	/**
	 * Default rule priority.
	 */
	int DEFAULT_PRIORITY = Integer.MAX_VALUE - 1;

	/**
	 * Getter for rule name.
	 * @return the rule name
	 */
	default String getName() {
		return DEFAULT_NAME;
	}

	/**
	 * Getter for rule description.
	 * @return rule description
	 */
	default String getDescription() {
		return DEFAULT_DESCRIPTION;
	}

	/**
	 * Getter for rule priority.
	 * @return rule priority
	 */
	default int getPriority() {
		return DEFAULT_PRIORITY;
	}

	/**
	 * This method implements the rule's condition(s). <strong>Implementations should
	 * handle any runtime exception and return true/false accordingly</strong>
	 * @return true if the rule should be applied given the provided facts, false
	 * otherwise
	 */
	boolean evaluate(Facts facts);

	/**
	 * This method implements the rule's action(s).
	 */
	void execute(Facts facts);

	/**
	 * This method apply facts to the rule, which is the combination of evaluation and
	 * execution.
	 * @return true if this rule is applied successfully, false otherwise
	 */
	boolean apply(Facts facts);

}
