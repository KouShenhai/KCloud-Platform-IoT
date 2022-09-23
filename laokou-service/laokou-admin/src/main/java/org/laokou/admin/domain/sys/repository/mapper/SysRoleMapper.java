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
package org.laokou.admin.domain.sys.repository.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.domain.sys.entity.SysRoleDO;
import org.laokou.admin.interfaces.qo.SysRoleQO;
import org.laokou.common.vo.SysRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * 角色类
 * @author  Kou Shenhai
 */
@Mapper
@Repository
public interface SysRoleMapper extends BaseMapper<SysRoleDO> {

    /**
     * 查询角色Ids
     * @return
     */
    List<Long> getRoleIds();

    /**
     * 通过userId查询角色Ids
     * @param userId
     * @return
     */
    List<Long> getRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据userId获取角色名称
     * @param userId
     * @return
     */
    List<SysRoleVO> getRoleListByUserId(@Param("userId")Long userId);

    /**
     * 分页查询角色
     * @param page
     * @param qo
     * @return
     */
    IPage<SysRoleVO> getRoleList(IPage<SysRoleVO> page, @Param("qo") SysRoleQO qo);

    SysRoleVO getRoleById(@Param("id") Long id);

    void deleteRole(@Param("id") Long id);

    List<SysRoleVO> getRoleList(@Param("qo") SysRoleQO qo);

}
