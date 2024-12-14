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
import java.util.Collections;

/**
 * @author cola
 */
public class AllRules extends CompositeRule {

	public static CompositeRule allOf(Rule... rules) {
		CompositeRule instance = new AllRules();
		Collections.addAll(instance.rules, rules);
		return instance;
	}

	@Override
	public boolean evaluate(Facts facts) {
		return rules.stream().allMatch(rule -> rule.evaluate(facts));
	}

	@Override
	public void execute(Facts facts) {
		for (Rule rule : rules) {
			rule.execute(facts);
		}
	}

	@Override
	protected boolean doApply(Facts facts) {
		if (evaluate(facts)) {
			for (Rule rule : rules) {
				// 所有的rules都执行
				rule.execute(facts);
			}
			return true;
		}
		return false;
	}

}
