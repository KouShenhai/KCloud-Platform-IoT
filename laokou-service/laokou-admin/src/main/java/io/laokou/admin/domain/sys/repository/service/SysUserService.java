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
package io.laokou.admin.domain.sys.repository.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.interfaces.dto.SysUserDTO;
import io.laokou.admin.interfaces.qo.SysUserQO;
import io.laokou.admin.interfaces.vo.OptionVO;
import io.laokou.admin.interfaces.vo.SysUserVO;
import org.laokou.common.user.UserDetail;
import io.laokou.admin.domain.sys.entity.SysUserDO;

import java.util.List;

/**
 * 用户类
 * @author Kou Shenhai
 */
public interface SysUserService extends IService<SysUserDO> {

    /**
     * 修改用户信息
     * @param
     */
    void updateUser(SysUserDTO dto);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    UserDetail getUserDetail(Long userId);

    /**
     * 根据openid获取username
     * @param zfbOpenid 支付宝唯一用户标识
     * @return
     */
    String getUsernameByOpenid(String zfbOpenid);

    /**
     * 分页查询用户
     * @param page
     * @param qo
     * @return
     */
    IPage<SysUserVO> getUserPage(IPage<SysUserVO> page, SysUserQO qo);

    /**
     * 根据id删除用户
     * @param id
     */
    void deleteUser(Long id);

    /**
     * 获取用户列表
     * @return
     */
    List<SysUserVO> getUserList();

    /**
     * 根据id获取用户列表
     * @param id
     * @return
     */
    List<SysUserVO> getUserListByUserId(Long id);

    /**
     * 获取用户列表下拉框
     * @return
     */
    List<OptionVO> getOptionList();
}
