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

package org.laokou.common.statemachine.impl;

import org.laokou.common.statemachine.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * StateHelper.
 *
 * @author Frank Zhang 2020-02-08 4:23 PM
 */
public class StateHelper {

	public static <S, E, C> State<S, E, C> getState(Map<S, State<S, E, C>> stateMap, S stateId) {
		State<S, E, C> state = stateMap.get(stateId);
		if (state == null) {
			state = new StateImpl<>(stateId);
			stateMap.put(stateId, state);
		}
		return state;
	}

	@SafeVarargs
	public static <C, E, S> List<State<S, E, C>> getStates(Map<S, State<S, E, C>> stateMap, S... stateIds) {
		List<State<S, E, C>> result = new ArrayList<>();
		for (S stateId : stateIds) {
			State<S, E, C> state = getState(stateMap, stateId);
			result.add(state);
		}
		return result;
	}

}
