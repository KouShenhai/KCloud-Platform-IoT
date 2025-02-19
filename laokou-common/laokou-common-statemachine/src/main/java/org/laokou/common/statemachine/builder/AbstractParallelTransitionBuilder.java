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

package org.laokou.common.statemachine.builder;

import org.laokou.common.statemachine.State;
import org.laokou.common.statemachine.impl.StateHelper;
import org.laokou.common.statemachine.impl.TransitionType;

import java.util.List;
import java.util.Map;

abstract class AbstractParallelTransitionBuilder<S, E, C> implements ParallelFrom<S, E, C>, On<S, E, C>, To<S, E, C> {

	final Map<S, State<S, E, C>> stateMap;

	final TransitionType transitionType;

	protected List<State<S, E, C>> targets;

	public AbstractParallelTransitionBuilder(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
		this.stateMap = stateMap;
		this.transitionType = transitionType;
	}

	@SafeVarargs
	@Override
	public final To<S, E, C> toAmong(S... stateIds) {
		targets = StateHelper.getStates(stateMap, stateIds);
		return this;
	}

}
