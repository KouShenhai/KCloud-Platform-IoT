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
 * This is Natural Rules.
 *
 * @author cola
 */
public class NaturalRules extends CompositeRule {

	public static CompositeRule of(Rule... rules) {
		CompositeRule instance = new NaturalRules();
		Collections.addAll(instance.rules, rules);
		return instance;
	}

	@Override
	public boolean evaluate(Facts facts) {
		// 不支持, which means Natural Rules can not be the children of other rules
		throw new RuntimeException("evaluate not supported for natural composite");
	}

	@Override
	public void execute(Facts facts) {
		// 不支持, which means Natural Rules can not be the children of other rules
		throw new RuntimeException("execute not supported for natural composite");
	}

	@Override
	protected boolean doApply(Facts facts) {
		for (Rule rule : rules) {
			rule.apply(facts);
		}
		return true;
	}

}
