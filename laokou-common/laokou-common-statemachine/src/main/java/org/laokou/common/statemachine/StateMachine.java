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

import java.util.List;

/**
 * StateMachine.
 *
 * @author Frank Zhang 2020-02-07 2:13 PM
 * @param <S> the type of state
 * @param <E> the type of event
 * @param <C> the user defined context
 */
public interface StateMachine<S, E, C> extends Visitable {

	/**
	 * Verify if an event {@code E} can be fired from current state {@code S}.
	 */
	boolean verify(S sourceStateId, E event);

	/**
	 * Send an event {@code E} to the state machine.
	 * @param sourceState the source state
	 * @param event the event to send
	 * @param ctx the user defined context
	 * @return the target state
	 */
	S fireEvent(S sourceState, E event, C ctx);

	List<S> fireParallelEvent(S sourceState, E event, C ctx);

	/**
	 * MachineId is the identifier for a State Machine.
	 */
	String getMachineId();

	/**
	 * Use visitor pattern to display the structure of the state machine.
	 */
	void showStateMachine();

	String generatePlantUML();

}
