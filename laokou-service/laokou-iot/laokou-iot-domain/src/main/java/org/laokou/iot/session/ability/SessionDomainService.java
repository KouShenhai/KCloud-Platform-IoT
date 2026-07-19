/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.session.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.session.gateway.SessionGateway;
import org.laokou.iot.session.model.SessionA;
import org.springframework.stereotype.Component;

/**
 * 会话领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SessionDomainService {

	private final SessionGateway sessionGateway;

	public void createSession(SessionA sessionA) {
		sessionGateway.createSession(sessionA);
	}

	public void updateSession(SessionA sessionA) {
		sessionGateway.updateSession(sessionA);
	}

	public void deleteSession(Long[] ids) {
		sessionGateway.deleteSession(ids);
	}

}
