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
 * 系统消息
 * @author laokou
 */
@Data
@TableName("boot_sys_message")
@Schema(name = "SysMessageDO",description = "系统消息实体类")
public class SysMessageDO extends BaseDO {

    /**
     * 消息标题
     */
    @Schema(name = "title",description = "消息标题")
    private String title;

    /**
     * 消息内容
     */
    @Schema(name = "content",description = "消息内容")
    private String content;

    /**
     * 发送渠道 0 平台 1 微信公众号 2 邮箱
     */
    @Schema(name = "sendChannel",description = "发送渠道 0 平台 1 微信公众号 2 邮箱")
    private Integer sendChannel;

    /**
     * 消息类型 0 通知 1 提醒
     */
    @Schema(name = "type",description = "消息类型 0 通知 1 提醒")
    private Integer type;

}
