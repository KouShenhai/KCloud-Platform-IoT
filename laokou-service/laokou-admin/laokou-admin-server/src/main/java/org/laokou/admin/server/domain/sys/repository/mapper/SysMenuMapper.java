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
import org.laokou.admin.server.interfaces.qo.SysMenuQo;
import org.laokou.admin.client.vo.SysMenuVO;
import org.laokou.admin.server.domain.sys.entity.SysMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 菜单类
 * @author laokou
 */
@Mapper
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenuDO> {

    /**
     * 获取所有的资源列表
     * @param type
     * @return
     */
    List<SysMenuVO> getMenuList(@Param("type")Integer type);

    /**
     * 通过userId查询资源权限
     * @param userId
     * @param type
     * @return
     */
    List<SysMenuVO> getMenuListByUserId(@Param("userId") Long userId, @Param("type")Integer type);

    /**
     * 查询菜单列表
     * @param qo
     * @return
     */
    List<SysMenuVO> queryMenuList(@Param("qo") SysMenuQo qo);

    /**
     * 根据id查询菜单
     * @param id
     * @return
     */
    SysMenuVO getMenuById(@Param("id") Long id);

    /**
     * 通过roleId获取菜单
     * @param roleId
     * @return
     */
    List<SysMenuVO> getMenuListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色id查询菜单ids
     * @param roleId
     * @return
     */
    List<Long> getMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(@Param("id") Long id);

}
