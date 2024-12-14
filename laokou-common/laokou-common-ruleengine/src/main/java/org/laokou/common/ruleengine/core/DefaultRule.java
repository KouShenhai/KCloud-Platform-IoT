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

/*
 * The MIT License
 *
 *  Copyright (c) 2021, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.laokou.common.ruleengine.core;

import org.laokou.common.ruleengine.api.Action;
import org.laokou.common.ruleengine.api.Condition;
import org.laokou.common.ruleengine.api.Facts;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cola
 */
public class DefaultRule extends AbstractRule {

	private final Condition condition;

	private final List<Action> actions;

	public DefaultRule(Condition condition, Action action) {
		this.condition = condition;
		this.actions = new ArrayList<>();
		this.actions.add(action);
	}

	public DefaultRule(Condition condition, List<Action> actions) {
		this.condition = condition;
		this.actions = actions;
	}

	public DefaultRule(String name, String description, int priority, Condition condition, List<Action> actions) {
		super(name, description, priority);
		this.condition = condition;
		this.actions = actions;
	}

	@Override
	public boolean evaluate(Facts facts) {
		return condition.evaluate(facts);
	}

	@Override
	public void execute(Facts facts) {
		for (Action action : actions) {
			action.execute(facts);
		}
	}

	@Override
	public boolean apply(Facts facts) {
		if (condition.evaluate(facts)) {
			for (Action action : actions) {
				action.execute(facts);
			}
			return true;
		}
		return false;
	}

}
