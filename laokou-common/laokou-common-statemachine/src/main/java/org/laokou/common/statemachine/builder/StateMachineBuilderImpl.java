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
import org.laokou.common.statemachine.StateMachine;
import org.laokou.common.statemachine.StateMachineFactory;
import org.laokou.common.statemachine.impl.StateMachineImpl;
import org.laokou.common.statemachine.impl.TransitionType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StateMachineBuilderImpl.
 *
 * @author Frank Zhang 2020-02-07 9:40 PM
 */
public class StateMachineBuilderImpl<S, E, C> implements StateMachineBuilder<S, E, C> {

	/**
	 * StateMap is the same with stateMachine, as the core of state machine is holding
	 * reference to states.
	 */
	private final Map<S, State<S, E, C>> stateMap = new ConcurrentHashMap<>();

	private final StateMachineImpl<S, E, C> stateMachine = new StateMachineImpl<>(stateMap);

	private FailCallback<S, E, C> failCallback = new NumbFailCallback<>();

	@Override
	public ExternalTransitionBuilder<S, E, C> externalTransition() {
		return new TransitionBuilderImpl<>(stateMap, TransitionType.EXTERNAL);
	}

	@Override
	public ExternalTransitionsBuilder<S, E, C> externalTransitions() {
		return new TransitionsBuilderImpl<>(stateMap, TransitionType.EXTERNAL);
	}

	@Override
	public ExternalParallelTransitionBuilder<S, E, C> externalParallelTransition() {
		return new ParallelTransitionBuilderImpl<>(stateMap, TransitionType.EXTERNAL);
	}

	@Override
	public InternalTransitionBuilder<S, E, C> internalTransition() {
		return new TransitionBuilderImpl<>(stateMap, TransitionType.INTERNAL);
	}

	@Override
	public void setFailCallback(FailCallback<S, E, C> callback) {
		this.failCallback = callback;
	}

	@Override
	public StateMachine<S, E, C> build(String machineId) {
		stateMachine.setMachineId(machineId);
		stateMachine.setReady(true);
		stateMachine.setFailCallback(failCallback);
		StateMachineFactory.register(stateMachine);
		return stateMachine;
	}

}
