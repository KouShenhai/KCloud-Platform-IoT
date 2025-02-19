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

import java.util.Collection;
import java.util.List;

/**
 * State.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 * @author Frank Zhang 2020-02-07 2:12 PM
 */
public interface State<S, E, C> extends Visitable {

	/**
	 * Gets the state identifier.
	 * @return the state identifiers
	 */
	S getId();

	/**
	 * Add transition to the state.
	 * @param event the event of the Transition
	 * @param target the target of the transition
	 */
	Transition<S, E, C> addTransition(E event, State<S, E, C> target, TransitionType transitionType);

	List<Transition<S, E, C>> addTransitions(E event, List<State<S, E, C>> targets, TransitionType transitionType);

	List<Transition<S, E, C>> getEventTransitions(E event);

	Collection<Transition<S, E, C>> getAllTransitions();

}
