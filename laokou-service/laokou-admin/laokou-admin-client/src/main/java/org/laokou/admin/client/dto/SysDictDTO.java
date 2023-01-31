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
package org.laokou.admin.client.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
/**
 * @author laokou
 */
@Data
public class SysDictDTO {

    private Long id;
    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不为空")
    private String dictLabel;
    /**
     * 字典类型
     */
    @NotBlank(message = "字典类型不为空")
    private String type;
    /**
     * 字典值
     */
    @NotBlank(message = "字典值不为空")
    private String dictValue;
    /**
     * 状态 0 正常 1 停用
     */
    @NotNull(message = "请选择字典状态")
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 字典排序
     */
    @NotNull(message = "字典排序不为空")
    private Integer sort;
}
