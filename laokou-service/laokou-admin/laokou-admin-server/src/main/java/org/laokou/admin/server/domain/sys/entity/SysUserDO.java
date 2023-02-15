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
import jakarta.validation.constraints.NotBlank;
import org.laokou.common.mybatisplus.entity.BaseDO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
/**
 * 系统用户
 * @author laokou
 */
@Data
@TableName("boot_sys_user")
@Schema(name = "SysUserDO",description = "系统用户实体类")
public class SysUserDO extends BaseDO {

    @NotBlank(message = "{sys.user.password.require}")
    @Schema(name = "password",description = "密码",example = "123456")
    @Length(min = 6, max = 18, message = "密码长度必须在 {min} - {max} 之间")
    private String password;

    /**
     * 用户名
     */
    @NotBlank(message = "{sys.user.username.require}")
    @Schema(name = "username",description = "用户名",example = "admin")
    private String username;

    /**
     * 超级管理员标识 0 否 1 是
     */
    @Schema(name = "superAdmin",description = "超级管理员标识 0 否 1 是",example = "1")
    private Integer superAdmin;

    /**
     * 用户头像URL
     */
    @Schema(name = "imgUrl",description = "用户头像URL",example = "https://pic.cnblogs.com/avatar/simple_avatar.gif")
    private String imgUrl;

    /**
     * 邮箱
     */
    @Schema(name = "mail",description = "邮箱",example = "2413176044@qq.com")
    private String mail;

    /**
     * 用户状态 0 正常 1 停用
     */
    @Schema(name = "status",description = "用户状态 0 正常 1 停用",example = "0")
    private Integer status;

    /**
     * 手机号
     */
    @Schema(name = "mobile",description = "手机号",example = "18974432578")
    private String mobile;

    /**
     * 部门id
     */
    @Schema(name = "deptId",description = "部门id",example = "0")
    private Long deptId;

    /**
     * 租户id
     */
    @Schema(name = "tenantId",description = "租户id")
    private Long tenantId;
}
