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

import org.laokou.common.statemachine.exception.TransitionFailException;

/**
 * Alert fail callback, throw an {@code TransitionFailException}.
 *
 * @author 龙也 2022/9/15 12:02 PM
 */
public class AlertFailCallback<S, E, C> implements FailCallback<S, E, C> {

	@Override
	public void onFail(S sourceState, E event, C context) {
		throw new TransitionFailException("Cannot fire event [" + event + "] on current state [" + sourceState
				+ "] with context [" + context + "]");
	}

}
