/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package io.laokou.admin.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.laokou.common.utils.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 公众号账号管理
*
 * @author limingze
 * @create: 2022-07-13 09:45
*/
@Data
@ApiModel(value = "公众号账号管理")
public class WXMpAccountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "AppID")
    private String appId;

    @ApiModelProperty(value = "AppSecret")
    private String appSecret;

    @ApiModelProperty(value = "Token")
    private String token;

    @ApiModelProperty(value = "EncodingAESKey")
    private String aesKey;

    @ApiModelProperty(value = "创建者")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createDate;
}