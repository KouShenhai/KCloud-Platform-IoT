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

package org.laokou.admin.i18nMessage.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMessage.convertor.I18nMessageConvertor;
import org.laokou.admin.i18nMessage.gateway.I18nMessageGateway;
import org.laokou.admin.i18nMessage.gatewayimpl.database.I18nMessageMapper;
import org.laokou.admin.i18nMessage.gatewayimpl.database.dataobject.I18nMessageDO;
import org.laokou.admin.i18nMessage.model.I18nMessageE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 国际化消息网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class I18nMessageGatewayImpl implements I18nMessageGateway {

	private final I18nMessageMapper i18nMessageMapper;

	@Override
	public void create(I18nMessageE i18nMessageE) {
		i18nMessageMapper.insert(I18nMessageConvertor.toDataObject(i18nMessageE));
	}

	@Override
	public void update(I18nMessageE i18nMessageE) {
		I18nMessageDO i18nMessageDO = I18nMessageConvertor.toDataObject(i18nMessageE);
		i18nMessageDO.setVersion(i18nMessageMapper.selectVersion(i18nMessageE.getId()));
		i18nMessageMapper.updateById(i18nMessageDO);
	}

	@Override
	public void delete(Long[] ids) {
		i18nMessageMapper.deleteByIds(Arrays.asList(ids));
	}

}
