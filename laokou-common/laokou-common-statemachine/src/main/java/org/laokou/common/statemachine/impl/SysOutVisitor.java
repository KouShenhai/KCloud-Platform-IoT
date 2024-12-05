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
 * SysOutVisitor.
 *
 * @author Frank Zhang 2020-02-08 8:48 PM
 */
public class SysOutVisitor implements Visitor {

	@Override
	public String visitOnEntry(StateMachine<?, ?, ?> stateMachine) {
		return "-----StateMachine:" + stateMachine.getMachineId() + "-------";
	}

	@Override
	public String visitOnExit(StateMachine<?, ?, ?> stateMachine) {
		return "------------------------";
	}

	@Override
	public String visitOnEntry(State<?, ?, ?> state) {
		StringBuilder sb = new StringBuilder();
		String stateStr = "State:" + state.getId();
		sb.append(stateStr).append(LF);
		for (Transition<?, ?, ?> transition : state.getAllTransitions()) {
			String transitionStr = "    Transition:" + transition;
			sb.append(transitionStr).append(LF);
		}
		return sb.toString();
	}

	@Override
	public String visitOnExit(State<?, ?, ?> visitable) {
		return "";
	}

}
