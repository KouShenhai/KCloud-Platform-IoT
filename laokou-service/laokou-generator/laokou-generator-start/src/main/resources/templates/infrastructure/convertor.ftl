// @formatter:off
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

package ${packageName}.${instanceName}.convertor;

import ${packageName}.${instanceName}.gatewayimpl.database.dataobject.${className}DO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import ${packageName}.${instanceName}.dto.clientobject.${className}CO;
import ${packageName}.${instanceName}.model.${className}E;

/**
 *
 * ${comment}转换器.
 *
 * @author ${author}
 */
public class ${className}Convertor {

	public static ${className}DO toDataObject(${className}E ${instanceName}E) {
		${className}DO ${instanceName}DO = ConvertUtil.sourceToTarget(${instanceName}E, ${className}DO.class);
		if (ObjectUtil.isNull(${instanceName}DO.getId())) {
			${instanceName}DO.generatorId();
		}
		return ${instanceName}DO;
	}

	public static ${className}CO toClientObject(${className}DO ${instanceName}DO) {
		return ConvertUtil.sourceToTarget(${instanceName}DO, ${className}CO.class);
	}

}
// @formatter:on
