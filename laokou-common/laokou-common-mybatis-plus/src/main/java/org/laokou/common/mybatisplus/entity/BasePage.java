/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.mybatisplus.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author laokou
 */
@Data
@Schema(name = "BasePage",description = "基础分页实体类")
public abstract class BasePage implements Serializable {

    @Serial
    private static final long serialVersionUID = 6412915892334241813L;
    /**
     * 页码
     */
    @NotNull(message = "显示页码不为空")
    @Schema(name = "pageNum",description = "页码")
    private Integer pageNum;

    /**
     *
     */
    @NotNull(message = "显示条数不为空")
    @Schema(name = "pageSize",description = "条数")
    private Integer pageSize;

    /**
     * sql拼接
     */
    @Schema(name = "sqlFilter",description = "sql拼接")
    private String sqlFilter;

}