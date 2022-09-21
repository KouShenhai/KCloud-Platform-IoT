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
package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.SysRoleDTO;
import io.laokou.admin.interfaces.qo.SysRoleQO;
import io.laokou.common.vo.SysRoleVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysRoleApplicationService {

    IPage<SysRoleVO> queryRolePage(SysRoleQO qo);

    List<SysRoleVO> getRoleList(SysRoleQO qo);

    SysRoleVO getRoleById(Long id);

    Boolean insertRole(SysRoleDTO dto, HttpServletRequest request);

    Boolean updateRole(SysRoleDTO dto, HttpServletRequest request);

    Boolean deleteRole(Long id);
}
