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

package org.laokou.common.statemachine.impl;

import org.laokou.common.statemachine.Action;
import org.laokou.common.statemachine.Condition;
import org.laokou.common.statemachine.State;
import org.laokou.common.statemachine.Transition;

/**
 * TransitionImpl。
 * <p>
 * This should be designed to be immutable, so that there is no thread-safe risk
 *
 * @author Frank Zhang 2020-02-07 10:32 PM
 */
public class TransitionImpl<S, E, C> implements Transition<S, E, C> {

	private State<S, E, C> source;

	private State<S, E, C> target;

	private E event;

	private Condition<C> condition;

	private Action<S, E, C> action;

	private TransitionType type = TransitionType.EXTERNAL;

	@Override
	public State<S, E, C> getSource() {
		return source;
	}

	@Override
	public void setSource(State<S, E, C> state) {
		this.source = state;
	}

	@Override
	public E getEvent() {
		return this.event;
	}

	@Override
	public void setEvent(E event) {
		this.event = event;
	}

	@Override
	public void setType(TransitionType type) {
		this.type = type;
	}

	@Override
	public State<S, E, C> getTarget() {
		return this.target;
	}

	@Override
	public void setTarget(State<S, E, C> target) {
		this.target = target;
	}

	@Override
	public Condition<C> getCondition() {
		return this.condition;
	}

	@Override
	public void setCondition(Condition<C> condition) {
		this.condition = condition;
	}

	@Override
	public Action<S, E, C> getAction() {
		return this.action;
	}

	@Override
	public void setAction(Action<S, E, C> action) {
		this.action = action;
	}

	@Override
	public State<S, E, C> transit(C ctx, boolean checkCondition) {
		this.verify();
		if (!checkCondition || condition == null || condition.isSatisfied(ctx)) {
			if (action != null) {
				action.execute(source.getId(), target.getId(), event, ctx);
			}
			return target;
		}
		return source;
	}

	@Override
	public final String toString() {
		return source + "-[" + event.toString() + ", " + type + "]->" + target;
	}

	@Override
	public boolean equals(Object anObject) {
		if (anObject instanceof Transition<?, ?, ?> other) {
			return this.event.equals(other.getEvent()) && this.source.equals(other.getSource())
					&& this.target.equals(other.getTarget());
		}
		return false;
	}

	@Override
	public void verify() {
		if (type == TransitionType.INTERNAL && source != target) {
			throw new StateMachineException(String.format(
					"Internal transition source state '%s' " + "and target state '%s' must be same.", source, target));
		}
	}

}
