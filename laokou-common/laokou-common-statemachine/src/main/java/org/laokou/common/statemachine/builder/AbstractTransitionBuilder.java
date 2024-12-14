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

package org.laokou.common.statemachine.builder;

import org.laokou.common.statemachine.State;
import org.laokou.common.statemachine.impl.StateHelper;

import org.laokou.common.statemachine.impl.TransitionType;

import java.util.Map;

/**
 * Take TransitionBuilderImpl and TransitionsBuilderImpl sharing variables and methods to
 * that abstract class, which acts as their parent, instead of having
 * TransitionsBuilderImpl inherit from TransitionsBuilderImpl. I think that the multi-flow
 * builder(TransitionsBuilderImpl) and single-flow builder(TransitionBuilderImpl) are
 * equal and not supposed to be parent-child relationship, they from, when, and perform
 * methods are not the same, and although it looks like just a set of loops but logically
 * should not be inherited over Override. ( Just as there was no relationship, why should
 * we talk to each other, say a we are not suitable). With the abstract class, multi-flow
 * and single-flow only use to focus on their respective functions are single-flow, or
 * multi-flow. Conform to a single duty.
 *
 * @author welliem 2023-07-14 12:13
 */
abstract class AbstractTransitionBuilder<S, E, C> implements From<S, E, C>, On<S, E, C>, To<S, E, C> {

	final Map<S, State<S, E, C>> stateMap;

	final TransitionType transitionType;

	protected State<S, E, C> target;

	public AbstractTransitionBuilder(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
		this.stateMap = stateMap;
		this.transitionType = transitionType;
	}

	@Override
	public To<S, E, C> to(S stateId) {
		target = StateHelper.getState(stateMap, stateId);
		return this;
	}

}
