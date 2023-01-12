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
package org.laokou.admin.server.domain.sys.repository.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.client.dto.SysUserDTO;
import org.laokou.admin.server.interfaces.qo.SysUserQo;
import org.laokou.admin.client.vo.OptionVO;
import org.laokou.admin.client.vo.SysUserVO;
import org.laokou.admin.server.domain.sys.entity.SysUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户类
 * @author laokou
 */
@Mapper
@Repository
public interface SysUserMapper extends BaseMapper<SysUserDO> {
    /**
     * 分页查询用户
     * @param page
     * @param qo
     * @return
     */
    IPage<SysUserVO> getUserPage(IPage<SysUserVO> page, @Param("qo") SysUserQo qo);

    /**
     * 更新用户
     * @param dto
     */
    void updateUser(@Param("dto") SysUserDTO dto);

    /**
     * 根据id删除用户
     * @param id
     */
    void deleteUser(@Param("id") Long id);

    /**
     * 获取下拉用户选择列表
     * @return
     */
    List<OptionVO> getOptionList();
}
