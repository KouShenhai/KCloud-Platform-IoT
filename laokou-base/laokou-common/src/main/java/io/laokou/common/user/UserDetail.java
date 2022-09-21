/**
 * Copyright 2020-2022 Kou Shenhai
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
package io.laokou.common.user;
import io.laokou.common.vo.SysDeptVO;
import io.laokou.common.vo.SysRoleVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Data
public class UserDetail implements Serializable {
    private Long id;
    private String username;
    private String imgUrl;
    private Integer superAdmin;
    private Integer status;
    private String email;
    private String mobile;
    private String password;
    private String zfbOpenid;
    private Long deptId;
    private List<String> permissionsList;
    private List<SysRoleVO> roles;
    /**
     * 数据权限
     */
    private List<SysDeptVO> depts;
}
