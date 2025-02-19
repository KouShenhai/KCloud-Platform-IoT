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

package org.laokou.common.statemachine;

import org.laokou.common.statemachine.impl.TransitionType;

/**
 * {@code Transition} is something what a state machine associates with a state changes.
 *
 * @author Frank Zhang 2020-02-07 2:20 PM
 * @param <S> the type of state
 * @param <E> the type of event
 * @param <C> the type of user defined context, which is used to hold application data
 *
 */
public interface Transition<S, E, C> {

	/**
	 * Gets the source state of this transition.
	 * @return the source state
	 */
	State<S, E, C> getSource();

	void setSource(State<S, E, C> state);

	E getEvent();

	void setEvent(E event);

	void setType(TransitionType type);

	/**
	 * Gets the target state of this transition.
	 * @return the target state
	 */
	State<S, E, C> getTarget();

	void setTarget(State<S, E, C> state);

	/**
	 * Gets the guard of this transition.
	 * @return the guard
	 */
	Condition<C> getCondition();

	void setCondition(Condition<C> condition);

	Action<S, E, C> getAction();

	void setAction(Action<S, E, C> action);

	/**
	 * Do transition from source state to target state.
	 * @return the target state
	 */

	State<S, E, C> transit(C ctx, boolean checkCondition);

	/**
	 * Verify transition correctness.
	 */
	void verify();

}
