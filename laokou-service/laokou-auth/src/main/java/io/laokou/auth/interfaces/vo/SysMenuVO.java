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
package io.laokou.auth.interfaces.vo;
import io.laokou.common.utils.TreeUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统菜单VO
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/1/16 0016 下午 9:08
 */
@Data
@ApiModel("系统菜单VO")
public class SysMenuVO extends TreeUtil.TreeNo<SysMenuVO> implements Serializable {

    /**
     * 类型   0：菜单   1：按钮
     */
    private Integer type;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 资源URL
     */
    private String url;
    /**
     * 请求方式（如：GET、POST、PUT、DELETE）
     */
    private String method;
    /**
     * 认证等级   0：权限认证   1：登录认证    2：无需认证
     */
    private Integer authLevel;
    /**
     * 权限标识
     */
    private String permissions;

}
