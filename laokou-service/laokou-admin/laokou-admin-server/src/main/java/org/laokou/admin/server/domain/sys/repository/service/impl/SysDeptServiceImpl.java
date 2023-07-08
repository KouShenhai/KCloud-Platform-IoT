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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.admin.server.domain.sys.entity.SysDeptDO;
import org.laokou.admin.server.domain.sys.repository.mapper.SysDeptMapper;
import org.laokou.admin.server.domain.sys.repository.service.SysDeptService;
import org.laokou.admin.server.interfaces.qo.SysDeptQo;
import org.laokou.admin.client.vo.SysDeptVO;
import org.laokou.auth.client.utils.UserUtil;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author laokou
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDeptDO> implements SysDeptService {

	@Override
	public List<SysDeptVO> getDeptList(SysDeptQo qo) {
		qo.setTenantId(UserUtil.getTenantId());
		return this.baseMapper.getDeptList(qo);
	}

	@Override
	public void deleteDept(Long id) {
		this.baseMapper.deleteById(id);
	}

	@Override
	public SysDeptVO getDept(Long id) {
		return this.baseMapper.getDept(id);
	}

	@Override
	public List<Long> getDeptIdsByRoleId(Long roleId) {
		return this.baseMapper.getDeptIdsByRoleId(roleId);
	}

	@Override
	public Integer getVersion(Long id) {
		return this.baseMapper.getVersion(id);
	}

}
