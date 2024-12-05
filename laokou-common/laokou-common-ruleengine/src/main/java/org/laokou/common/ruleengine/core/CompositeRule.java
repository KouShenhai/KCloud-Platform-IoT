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

package org.laokou.common.ruleengine.core;

import org.laokou.common.ruleengine.api.Facts;
import org.laokou.common.ruleengine.api.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cola
 */
public abstract class CompositeRule extends AbstractRule {

	protected List<Rule> rules = new ArrayList<>();

	private boolean isSorted = false;

	public CompositeRule() {

	}

	public CompositeRule priority(int priority) {
		this.priority = priority;
		return this;
	}

	public CompositeRule name(String name) {
		this.name = name;
		return this;
	}

	@Override
	public boolean apply(Facts facts) {
		sort();
		return doApply(facts);
	}

	protected abstract boolean doApply(Facts facts);

	protected void sort() {
		if (!isSorted) {
			Collections.sort(rules);
			isSorted = true;
		}
	}

}
