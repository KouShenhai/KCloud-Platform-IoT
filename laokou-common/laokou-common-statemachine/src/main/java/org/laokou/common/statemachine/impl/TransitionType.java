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

/**
 * TransitionType.
 *
 * @author Frank Zhang 2020-02-07 10:23 PM
 */
public enum TransitionType {

	/**
	 * Implies that the Transition, if triggered, occurs without exiting or entering the
	 * source State (i.e., it does not cause a state change). This means that the entry or
	 * exit condition of the source State will not be invoked. An internal Transition can
	 * be taken even if the SateMachine is in one or more Regions nested within the
	 * associated State.
	 */
	INTERNAL,
	/**
	 * Implies that the Transition, if triggered, will not exit the composite (source)
	 * State, but it will exit and re-enter any state within the composite State that is
	 * in the current state configuration.
	 */
	LOCAL,
	/**
	 * Implies that the Transition, if triggered, will exit the composite (source) State.
	 */
	EXTERNAL

}
