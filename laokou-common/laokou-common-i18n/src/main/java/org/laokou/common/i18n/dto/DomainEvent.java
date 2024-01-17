/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.dto;

import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.io.Serializable;
import java.time.Clock;

/**
 * 领域事件.
 * @author laokou
 */
public abstract class DomainEvent extends ApplicationEvent implements Serializable {

	@Serial
	private static final long serialVersionUID = 1532877866226749304L;

	protected DomainEvent(Object source, Clock clock) {
		super(source, clock);
	}

	protected DomainEvent(Object source) {
		super(source);
	}

}
