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

package ${packageName}.${instanceName}.convertor;

import ${packageName}.${instanceName}.gatewayimpl.database.dataobject.${className}DO;
import ${packageName}.${instanceName}.dto.clientobject.${className}CO;
import ${packageName}.${instanceName}.model.${className}E;

/**
 *
 * ${comment}转换器.
 *
 * @author ${author}
 */
public class ${className}Convertor {

	public static ${className}DO toDataObject(${className}E ${instanceName}E, boolean isInsert) {
        ${className}DO ${instanceName}DO = new ${className}DO();
		if (isInsert) {
			${instanceName}DO.generatorId();
		} else {
            ${instanceName}DO.setId(${instanceName}E.getId());
        }
        <#list fields as field>
        ${instanceName}DO.set${field.fieldName?cap_first}(${instanceName}E.get${field.fieldName?cap_first}());
        </#list>
        return ${instanceName}DO;
	}

	public static ${className}CO toClientObject(${className}DO ${instanceName}DO) {
        ${className}CO ${instanceName}CO = new ${className}CO();
        <#list fields as field>
            ${instanceName}CO.set${field.fieldName?cap_first}(${instanceName}DO.get${field.fieldName?cap_first}());
        </#list>
		return ${instanceName}CO;
	}

	public static ${className}E toEntity(${className}CO ${instanceName}CO) {
        ${className}E ${instanceName}E = new ${className}E();
        <#list fields as field>
            ${instanceName}E.set${field.fieldName?cap_first}(${instanceName}CO.get${field.fieldName?cap_first}());
        </#list>
		return ${instanceName}E;
	}

}
// @formatter:on
