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
package org.laokou.admin.domain.sys.repository.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.domain.sys.entity.SysMenuDO;
import org.laokou.admin.interfaces.qo.SysMenuQO;
import org.laokou.admin.interfaces.vo.SysMenuVO;
import org.laokou.common.user.UserDetail;
import java.util.List;
/**
 * 菜单类
 * @author Kou Shenhai
 */
public interface SysMenuService extends IService<SysMenuDO> {

    /**
     * 获取所有资源列表
     * @param userId
     * @return
     */
    List<SysMenuVO> getMenuList(Long userId, Integer type);

    /**
     * 查询所有权限列表
     * @return
     */
    List<String> getPermissionsList();

    /**
     * 查询用户权限列表
     * @param userId
     * @return
     */
    List<String> getPermissionsListByUserId(Long userId);

    /**
     * 获取菜单列表
     * @param userDetail
     * @param noCache
     * @param type
     * @return
     */
    List<SysMenuVO> getMenuList(UserDetail userDetail, boolean noCache, Integer type);

    /**
     * 查询列表
     * @param qo
     * @return
     */
    List<SysMenuVO> queryMenuList(SysMenuQO qo);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysMenuVO getMenuById(Long id);

    /**
     * 逻辑删除
     */
    void deleteMenu(Long id);

    /**
     * 通过roleId获取菜单
     * @param roleId
     * @return
     */
    List<SysMenuVO> getMenuListByRoleId(Long roleId);

    List<Long> getMenuIdsByRoleId(Long roleId);

}
