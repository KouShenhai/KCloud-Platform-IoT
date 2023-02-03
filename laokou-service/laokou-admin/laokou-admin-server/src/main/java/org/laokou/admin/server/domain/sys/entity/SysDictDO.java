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
package org.laokou.admin.server.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.common.mybatisplus.entity.BaseDO;
import lombok.Data;
/**
 * 系统字典
 * @author laokou
 */
@Data
@TableName("boot_sys_dict")
@Schema(name = "SysDictDO",description = "系统字典实体类")
public class SysDictDO extends BaseDO {

    /**
     * 字典名称
     */
    @Schema(name = "dictLabel",description = "字典名称")
    private String dictLabel;
    /**
     * 字典类型
     */
    @Schema(name = "type",description = "字典类型")
    private String type;
    /**
     * 字典值
     */
    @Schema(name = "dictValue",description = "字典值")
    private String dictValue;
    /**
     * 字典状态 0 正常 1 停用
     */
    @Schema(name = "status",description = "字典状态 0 正常 1 停用",example = "0")
    private Integer status;
    /**
     * 字典备注
     */
    @Schema(name = "remark",description = "字典备注",example = "OSS枚举")
    private String remark;
    /**
     * 字典排序
     */
    @Schema(name = "sort",description = "字典排序",example = "1")
    private Integer sort;

}
