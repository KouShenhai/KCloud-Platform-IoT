/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
 *
 */
package org.laokou.admin.server.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.admin.server.domain.sys.entity.SysRoleDO;
import org.laokou.admin.server.domain.sys.repository.mapper.SysRoleMapper;
import org.laokou.admin.server.domain.sys.repository.service.SysRoleService;
import org.laokou.admin.server.interfaces.qo.SysRoleQo;
import org.laokou.admin.vo.SysRoleVO;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author laokou
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {

	@Override
	public List<Long> getRoleIdsByUserId(Long userId) {
		return this.baseMapper.getRoleIdsByUserId(userId);
	}

	@Override
	public IPage<SysRoleVO> getRolePage(IPage<SysRoleVO> page, SysRoleQo qo) {
		return this.baseMapper.getRoleList(page, qo);
	}

	@Override
	public List<SysRoleVO> getRoleList(SysRoleQo qo) {
		return this.baseMapper.getRoleList(qo);
	}

	@Override
	public SysRoleVO getRoleById(Long id) {
		return this.baseMapper.getRoleById(id);
	}

	@Override
	public void deleteRole(Long id) {
		this.baseMapper.deleteById(id);
	}

	@Override
	public Integer getVersion(Long id) {
		return baseMapper.getVersion(id);
	}

}
