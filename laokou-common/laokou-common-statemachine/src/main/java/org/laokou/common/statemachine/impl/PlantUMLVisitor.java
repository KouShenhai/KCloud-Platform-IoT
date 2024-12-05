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
import org.laokou.common.statemachine.StateMachine;
import org.laokou.common.statemachine.Transition;
import org.laokou.common.statemachine.Visitor;

/**
 * PlantUMLVisitor.
 *
 * @author Frank Zhang 2020-02-09 7:47 PM
 */
public class PlantUMLVisitor implements Visitor {

	/**
	 * Since the state machine is stateless, there is no initial state.
	 * <p>
	 * You have to add "[*] -> initialState" to mark it as a state machine diagram.
	 * otherwise it will be recognized as a sequence diagram.
	 * @param visitable the element to be visited.
	 */
	@Override
	public String visitOnEntry(StateMachine<?, ?, ?> visitable) {
		return "@startuml" + LF;
	}

	@Override
	public String visitOnExit(StateMachine<?, ?, ?> visitable) {
		return "@enduml";
	}

	@Override
	public String visitOnEntry(State<?, ?, ?> state) {
		StringBuilder sb = new StringBuilder();
		for (Transition<?, ?, ?> transition : state.getAllTransitions()) {
			sb.append(transition.getSource().getId())
				.append(" --> ")
				.append(transition.getTarget().getId())
				.append(" : ")
				.append(transition.getEvent())
				.append(LF);
		}
		return sb.toString();
	}

	@Override
	public String visitOnExit(State<?, ?, ?> state) {
		return "";
	}

}
