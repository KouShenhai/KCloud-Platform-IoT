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
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
/**
 * 基础实体类，所有实体都需要继承
 * @author laokou
 */
@Data
@Schema(name = "BaseDO",description = "基础对象实体类")
public abstract class BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5855413730985647400L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @Schema(name = "id",description = "编号")
    private Long id;

    /**
     * 创建人
     */
    @Schema(name = "creator",description = "创建人")
    private Long creator;

    /**
     * 修改人
     */
    @Schema(name = "editor",description = "修改人")
    private Long editor;

    /**
     * 创建时间
     */
    @Schema(name = "createDate",description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 修改时间
     */
    @Schema(name = "updateDate",description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标识 0 未删除 1已删除
     */
    @Schema(name = "delFlag",description = "删除标识 0 未删除 1已删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer delFlag;

    /**
     * 版本号
     */
    @Version
    @Schema(name = "version",description = "版本号")
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

}