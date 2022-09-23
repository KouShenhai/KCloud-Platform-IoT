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
package org.laokou.admin.application.service;

import org.laokou.admin.interfaces.dto.SysDeptDTO;
import org.laokou.admin.interfaces.qo.SysDeptQO;
import org.laokou.common.vo.SysDeptVO;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:29
 */
public interface SysDeptApplicationService {

    SysDeptVO getDeptList();

    List<SysDeptVO> queryDeptList(SysDeptQO qo);

    Boolean insertDept(SysDeptDTO dto, HttpServletRequest request);

    Boolean updateDept(SysDeptDTO dto, HttpServletRequest request);

    Boolean deleteDept(Long id);

    SysDeptVO getDept(Long id);

    List<Long> getDeptIdsByRoleId(Long roleId);
}
