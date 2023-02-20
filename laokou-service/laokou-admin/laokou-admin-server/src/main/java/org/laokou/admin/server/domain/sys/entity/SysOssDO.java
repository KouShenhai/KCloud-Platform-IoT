/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
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
import lombok.Data;
import org.laokou.common.mybatisplus.entity.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_oss")
@Schema(name = "SysOssDO",description = "系统存储实体类")
public class SysOssDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 7064643286240062439L;
    /**
     * 名称
     */
    @Schema(name = "name",description = "名称")
    private String name;

    /**
     * 终端地址
     */
    @Schema(name = "endpoint",description = "终端地址")
    private String endpoint;

    /**
     * 区域
     */
    @Schema(name = "region",description = "区域")
    private String region;

    /**
     * 访问密钥
     */
    @Schema(name = "accessKey",description = "访问密钥")
    private String accessKey;

    /**
     * 用户密钥
     */
    @Schema(name = "secretKey",description = "用户密钥")
    private String secretKey;

    /**
     * 桶名
     */
    @Schema(name = "bucketName",description = "桶名")
    private String bucketName;

    /**
     * 路径样式访问 1已开启 0未启用
     */
    @Schema(name = "pathStyleAccessEnabled",description = "路径样式访问 1已开启 0未启用")
    private Integer pathStyleAccessEnabled;

    /**
     * 状态 1已启用 0未启用
     */
    @Schema(name = "status",description = "状态 1启用 0未启用")
    private Integer status;

}
