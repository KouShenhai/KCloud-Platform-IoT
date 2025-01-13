// @formatter:off
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

package ${packageName}.${instanceName}.gatewayimpl;

import lombok.RequiredArgsConstructor;
import ${packageName}.${instanceName}.model.${className}E;
import org.springframework.stereotype.Component;
import ${packageName}.${instanceName}.gateway.${className}Gateway;
import ${packageName}.${instanceName}.gatewayimpl.database.${className}Mapper;
import java.util.Arrays;
import ${packageName}.${instanceName}.convertor.${className}Convertor;
import ${packageName}.${instanceName}.gatewayimpl.database.dataobject.${className}DO;

/**
*
* ${comment}网关实现.
*
* @author ${author}
*/
@Component
@RequiredArgsConstructor
public class ${className}GatewayImpl implements ${className}Gateway {

	private final ${className}Mapper ${instanceName}Mapper;

    @Override
	public void create(${className}E ${instanceName}E) {
        ${instanceName}Mapper.insert(${className}Convertor.toDataObject(${instanceName}E, true));
	}

    @Override
	public void update(${className}E ${instanceName}E) {
		${className}DO ${instanceName}DO = ${className}Convertor.toDataObject(${instanceName}E, false);
		${instanceName}DO.setVersion(${instanceName}Mapper.selectVersion(${instanceName}E.getId()));
        ${instanceName}Mapper.updateById(${instanceName}DO);
	}

    @Override
	public void delete(Long[] ids) {
        ${instanceName}Mapper.deleteByIds(Arrays.asList(ids));
	}

}
// @formatter:on
