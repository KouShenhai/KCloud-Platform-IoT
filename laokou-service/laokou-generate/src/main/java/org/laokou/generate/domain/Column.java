/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.generate.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import lombok.experimental.SuperBuilder;

/**
 * @author laokou
 */
@Value
@SuperBuilder
public class Column {

    @Schema(name = "name", description = "列名")
    String name;

    @Schema(name = "type", description = "类型")
    String type;

    @Schema(name = "comment", description = "备注")
    String comment;

    @Schema(name = "attrName", description = "属性名称")
    String attrName;

    @Schema(name = "attrType", description = "属性类型")
    String attrType;

}
