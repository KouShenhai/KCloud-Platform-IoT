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

package org.laokou.common.statemachine;

import org.laokou.common.statemachine.impl.StateMachineException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StateMachineFactory.
 *
 * @author Frank Zhang 2020-02-08 10:21 PM
 */
public class StateMachineFactory {

	static Map<String /* machineId */, StateMachine<?, ?, ?>> stateMachineMap = new ConcurrentHashMap<>();

	public static void register(StateMachine<?, ?, ?> stateMachine) {
		String machineId = stateMachine.getMachineId();
		if (stateMachineMap.get(machineId) != null) {
			throw new StateMachineException(
					"The state machine with id [" + machineId + "] is already built, no need to build again");
		}
		stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
	}

	public static StateMachine<?, ?, ?> get(String machineId) {
		StateMachine<?, ?, ?> stateMachine = stateMachineMap.get(machineId);
		if (stateMachine == null) {
			throw new StateMachineException(
					"There is no stateMachine instance for " + machineId + ", please build it first");
		}
		return stateMachine;
	}

}
