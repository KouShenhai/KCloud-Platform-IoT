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
package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.SysMenuDTO;
import io.laokou.admin.interfaces.qo.SysMenuQO;
import io.laokou.admin.interfaces.vo.SysMenuVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysMenuApplicationService {

    SysMenuVO getMenuList(HttpServletRequest request);

    List<SysMenuVO> queryMenuList(SysMenuQO dto);

    SysMenuVO getMenuById(Long id);

    Boolean updateMenu(SysMenuDTO dto, HttpServletRequest request);

    Boolean insertMenu(SysMenuDTO dto, HttpServletRequest request);

    Boolean deleteMenu(Long id);

    SysMenuVO treeMenu(Long roleId);

    List<Long> getMenuIdsByRoleId(Long roleId);
}
