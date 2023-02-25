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
package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.client.dto.SysUserDTO;
import org.laokou.admin.client.vo.UserInfoVO;
import org.laokou.admin.server.interfaces.qo.SysUserQo;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.admin.client.vo.SysUserVO;
import java.util.List;

/**
 * @author laokou
 */
public interface SysUserApplicationService {

    /**
     * 修改用户
     * @param dto
     * @return
     */
    Boolean updateUser(SysUserDTO dto);

    /**
     * 修改密码
     * @param id
     * @param newPassword
     * @return
     */
    Boolean updatePassword(Long id,String newPassword);

    /**
     * 修改状态
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(Long id,Integer status);

    /**
     * 修改个人信息
     * @param dto
     * @return
     */
    Boolean updateInfo(SysUserDTO dto);

    /**
     * 新增用户
     * @param dto
     * @return
     */
    Boolean insertUser(SysUserDTO dto);

    /**
     * 分页查询用户
     * @param qo
     * @return
     */
    IPage<SysUserVO> queryUserPage(SysUserQo qo);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    SysUserVO getUserById(Long id);

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    Boolean deleteUser(Long id);

    /**
     * 用户下拉选择列表
     * @return
     */
    List<OptionVO> getOptionList();

    /**
     * 获取用户信息
     * @return
     */
    UserInfoVO getUserInfo();

}
