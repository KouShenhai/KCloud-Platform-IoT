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

package ${packageName}.${instanceName}.dto;

import lombok.*;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 *
 * 删除${comment}命令.
 *
 * @author ${author}
 */
@Getter
@RequiredArgsConstructor
public class ${className}RemoveCmd extends CommonCommand {

	private final Long[] ids;

}
// @formatter:on
