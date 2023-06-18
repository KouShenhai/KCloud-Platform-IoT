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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.client.dto.SysUserDTO;
import org.laokou.admin.server.interfaces.qo.SysUserQo;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.admin.client.vo.SysUserVO;
import org.laokou.admin.server.domain.sys.entity.SysUserDO;
import java.util.List;

/**
 * 用户类
 *
 * @author laokou
 */
public interface SysUserService extends IService<SysUserDO> {

	/**
	 * 修改用户
	 * @param dto
	 * @return
	 */
	void updateUser(SysUserDTO dto);

	/**
	 * 分页查询用户
	 * @param page
	 * @param qo
	 * @return
	 */
	IPage<SysUserVO> getUserPage(IPage<SysUserVO> page, SysUserQo qo);

	/**
	 * 根据id删除用户
	 * @param id
	 */
	void deleteUser(Long id);

	/**
	 * 获取用户列表下拉框
	 * @param tenantId
	 * @return
	 */
	List<OptionVO> getOptionList(Long tenantId);

	/**
	 * 获取版本号
	 * @param id
	 * @return
	 */
	Integer getVersion(Long id);

}
