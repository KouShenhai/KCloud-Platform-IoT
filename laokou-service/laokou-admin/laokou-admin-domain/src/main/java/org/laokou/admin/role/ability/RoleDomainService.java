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

package org.laokou.admin.role.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.role.gateway.*;
import org.laokou.admin.role.model.RoleE;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 角色领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleDomainService {

	private final RoleGateway roleGateway;

	private final RoleMenuGateway roleMenuGateway;

	public void create(RoleE roleE) {
		roleGateway.create(roleE);
	}

	public Mono<Void> update(RoleE roleE) {
		return roleGateway.update(roleE);
	}

	public Flux<Void> updateAuthority(RoleE roleE) {
		return Flux.merge(roleGateway.update(roleE), roleMenuGateway.update(roleE));
	}

	public Flux<Void> delete(Long[] ids) {
		return Flux.merge(roleGateway.delete(ids), roleMenuGateway.delete(ids));
	}

}
