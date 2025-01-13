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

import org.laokou.common.statemachine.StateMachine;

/**
 * StateMachineBuilder.
 *
 * @author Frank Zhang 2020-02-07 5:32 PM
 */
public interface StateMachineBuilder<S, E, C> {

	/**
	 * Builder for one transition.
	 * @return External transition builder
	 */
	ExternalTransitionBuilder<S, E, C> externalTransition();

	/**
	 * Builder for multiple transitions.
	 * @return External transition builder
	 */
	ExternalTransitionsBuilder<S, E, C> externalTransitions();

	/**
	 * Builder for parallel transitions.
	 * @return External transition builder
	 */
	ExternalParallelTransitionBuilder<S, E, C> externalParallelTransition();

	/**
	 * Start to build internal transition.
	 * @return Internal transition builder
	 */
	InternalTransitionBuilder<S, E, C> internalTransition();

	/**
	 * set up fail callback, default do nothing {@code NumbFailCallbackImpl}.
	 *
	 */
	void setFailCallback(FailCallback<S, E, C> callback);

	StateMachine<S, E, C> build(String machineId);

}
