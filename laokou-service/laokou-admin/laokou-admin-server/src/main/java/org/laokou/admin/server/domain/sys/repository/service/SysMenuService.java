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
package org.laokou.admin.server.domain.sys.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.server.domain.sys.entity.SysMenuDO;
import org.laokou.admin.server.interfaces.qo.SysMenuQo;
import org.laokou.admin.client.vo.SysMenuVO;
import org.laokou.auth.client.user.UserDetail;
import java.util.List;

/**
 * 菜单类
 *
 * @author laokou
 */
public interface SysMenuService extends IService<SysMenuDO> {

	/**
	 * 获取菜单列表
	 * @param userDetail
	 * @param type
	 * @return
	 */
	List<SysMenuVO> getMenuList(UserDetail userDetail, Integer type);

	/**
	 * 查询列表
	 * @param qo
	 * @return
	 */
	List<SysMenuVO> queryMenuList(SysMenuQo qo);

	/**
	 * 根据id查询菜单
	 * @param id
	 * @return
	 */
	SysMenuVO getMenuById(Long id);

	/**
	 * 根据id删除菜单
	 * @param id
	 */
	void deleteMenu(Long id);

	/**
	 * 根据角色id查询菜单ids
	 * @param roleId
	 * @return
	 */
	List<Long> getMenuIdsByRoleId(Long roleId);

	/**
	 * 获取版本号
	 * @param id
	 * @return
	 */
	Integer getVersion(Long id);

	/**
	 * 获取租户菜单
	 * @return
	 */
	List<SysMenuVO> getTenantMenuList();

}
