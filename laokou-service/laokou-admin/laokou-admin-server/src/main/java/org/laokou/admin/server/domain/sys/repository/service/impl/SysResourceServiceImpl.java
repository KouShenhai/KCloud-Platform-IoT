/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 *//*
	 *
	 * package org.laokou.admin.server.domain.sys.repository.service.impl;
	 *
	 * import com.baomidou.mybatisplus.core.metadata.IPage; import
	 * com.baomidou.mybatisplus.extension.service.impl.ServiceImpl; import
	 * org.apache.ibatis.session.ResultHandler; import
	 * org.laokou.admin.server.domain.sys.entity.SysResourceDO; import
	 * org.laokou.admin.server.domain.sys.repository.mapper.SysResourceMapper; import
	 * org.laokou.admin.server.domain.sys.repository.service.SysResourceService; import
	 * org.laokou.admin.server.infrastructure.index.ResourceIndex; import
	 * org.laokou.admin.server.interfaces.qo.SysResourceQo; import
	 * org.laokou.admin.client.vo.SysResourceVO; import
	 * org.springframework.stereotype.Service;
	 *
	 */
/**
 * @author laokou
 *//*
	 *
	 * @Service public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper,
	 * SysResourceDO> implements SysResourceService {
	 *
	 * @Override public IPage<SysResourceVO> getResourceList(IPage<SysResourceVO> page,
	 * SysResourceQo qo) { return this.baseMapper.getResourceList(page, qo); }
	 *
	 * @Override public SysResourceVO getResourceById(Long id) { return
	 * this.baseMapper.getResourceById(id); }
	 *
	 * @Override public void deleteResource(Long id) { this.baseMapper.deleteById(id); }
	 *
	 * @Override public Long getResourceTotal(String code) { return
	 * this.baseMapper.getResourceTotal(code); }
	 *
	 * @Override public SysResourceVO getResourceAuditByResourceId(Long id) { return
	 * baseMapper.getResourceAuditByResourceId(id); }
	 *
	 * @Override public Integer getVersion(Long id) { return
	 * this.baseMapper.getVersion(id); }
	 *
	 * @Override public void resultList(String code, ResultHandler<ResourceIndex>
	 * resultHandler) { this.baseMapper.resultList(code, resultHandler); }
	 *
	 * }
	 */
