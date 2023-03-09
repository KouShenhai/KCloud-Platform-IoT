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
package org.laokou.admin.client.vo;
import lombok.Data;
import org.laokou.common.jasypt.annotation.JasyptField;
import org.laokou.common.jasypt.enums.TypeEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户信息VO
 * @author laokou
 */
@Data
public class UserInfoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5297753219988591611L;
    private Long userId;
    private String imgUrl;
    @JasyptField(type = TypeEnum.DECRYPT)
    private String username;
    @JasyptField(type = TypeEnum.DECRYPT)
    private String mobile;
    @JasyptField(type = TypeEnum.DECRYPT)
    private String mail;
    private List<String> permissionList;
    private Long tenantId;
}
