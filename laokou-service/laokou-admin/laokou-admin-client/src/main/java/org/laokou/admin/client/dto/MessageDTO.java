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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
/**
 * @author laokou
 */
@Data
public class MessageDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7746946589804991815L;
    /**
     * 接收者
     */
    @NotEmpty(message = "所选接收人不少于一个，请重新选择")
    private Set<String> receiver;

    @NotBlank(message = "请输入标题")
    private String title;

    @NotBlank(message = "请输入内容")
    private String content;

    /**
     * 0通知 1提醒
     */
    @NotNull(message = "请选择消息类型")
    private Integer type;

}
