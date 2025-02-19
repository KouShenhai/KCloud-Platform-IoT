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

package org.laokou.admin.i18nMessage.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMessage.gateway.*;
import org.laokou.admin.i18nMessage.model.I18nMessageE;
import org.springframework.stereotype.Component;

/**
 * 国际化消息领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class I18nMessageDomainService {

	private final I18nMessageGateway i18nMessageGateway;

	public void create(I18nMessageE i18nMessageE) {
		i18nMessageGateway.create(i18nMessageE);
	}

	public void update(I18nMessageE i18nMessageE) {
		i18nMessageGateway.update(i18nMessageE);
	}

	public void delete(Long[] ids) {
		i18nMessageGateway.delete(ids);
	}

}
