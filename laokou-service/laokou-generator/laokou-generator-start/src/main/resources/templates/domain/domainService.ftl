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

package ${packageName}.${instanceName}.ability;

import lombok.RequiredArgsConstructor;
import ${packageName}.${instanceName}.gateway.*;
import ${packageName}.${instanceName}.model.${className}E;
import org.springframework.stereotype.Component;

/**
 *
 * ${comment}领域服务.
 *
 * @author ${author}
 */
@Component
@RequiredArgsConstructor
public class ${className}DomainService {

	private final ${className}Gateway ${instanceName}Gateway;

	public void create(${className}E ${instanceName}E) {
		${instanceName}Gateway.create(${instanceName}E);
	}

    public void update(${className}E ${instanceName}E) {
		${instanceName}Gateway.update(${instanceName}E);
	}

	public void delete(Long[] ids) {
		${instanceName}Gateway.delete(ids);
	}

}
// @formatter:on
