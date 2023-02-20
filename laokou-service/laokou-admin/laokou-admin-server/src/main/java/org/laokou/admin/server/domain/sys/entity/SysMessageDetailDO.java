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

import java.io.Serial;

/**
 * 系统消息详情
 * @author laokou
 */
@Data
@TableName("boot_sys_message_detail")
@Schema(name = "SysMessageDetailDO",description = "系统消息详情实体类")
public class SysMessageDetailDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 7760123944108929889L;
    /**
     * 消息id
     */
    @Schema(name = "messageId",description = "消息id")
    private Long messageId;

    /**
     * 用户id
     */
    @Schema(name = "userId",description = "用户id")
    private Long userId;

    /**
     * 查看标志 0 未查看 1 已查看
     */
    @Schema(name = "readFlag",description = "查看标志 0 未查看 1 已查看")
    private Integer readFlag;

}
