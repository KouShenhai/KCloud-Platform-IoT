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

package ${packageName}.${instanceName}.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * ${comment}客户端对象.
 *
 * @author ${author}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "${comment}客户端对象", description = "${comment}客户端对象")
public class ${className}CO extends ClientObject {

    @Schema(name = "ID", description = "ID")
	private Long id;
<#list fields as field>

    @Schema(name = "${field.comment}", description = "${field.comment}
	private ${field.fieldType} ${field.fieldName};
</#list>

    @Schema(name = "创建时间", description = "创建时间")
    private Instant createTime;

}
// @formatter:on
