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
import io.laokou.admin.interfaces.dto.SysUserDTO;
import io.laokou.admin.interfaces.qo.SysUserQO;
import io.laokou.admin.interfaces.vo.OptionVO;
import io.laokou.admin.interfaces.vo.SysUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysUserApplicationService {

    /**
     * 修改用户信息
     * @param
     */
    Boolean updateUser(SysUserDTO dto, HttpServletRequest request);

    Boolean insertUser(SysUserDTO dto, HttpServletRequest request);

    IPage<SysUserVO> queryUserPage(SysUserQO qo);

    SysUserVO getUserById(Long id);

    Boolean deleteUser(Long id,HttpServletRequest request);

    List<OptionVO> getOptionList();

}
