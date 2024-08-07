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

package org.laokou.auth.convertor;

import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.auth.model.LogV;
import org.laokou.common.i18n.dto.Convertor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * @author laokou
 */
@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG)
public interface LoginLogConvertor extends Convertor<LoginEvent, LogV, LoginLogDO> {

}
