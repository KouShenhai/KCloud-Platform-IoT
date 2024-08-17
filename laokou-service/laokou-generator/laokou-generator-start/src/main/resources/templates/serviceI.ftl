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

package ${packageName}.${instanceName}.api;

import ${packageName}.${instanceName}.dto.*;
import ${packageName}.${instanceName}.dto.clientobject.${className}CO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * @author ${author}
 */
public interface ${className}sServiceI {

	void save(${className}SaveCmd cmd);

	void modify(${className}ModifyCmd cmd);

	void remove(${className}RemoveCmd cmd);

	void export(${className}ExportCmd cmd);

	Result<Page<${className}CO>> page(${className}PageQry qry);

	Result<${className}CO> getById(${className}GetQry qry);

}
// @formatter:on
