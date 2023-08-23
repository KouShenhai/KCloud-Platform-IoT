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
	 * package org.laokou.admin.server.domain.sys.repository.service;
	 *
	 * import com.baomidou.mybatisplus.core.metadata.IPage; import
	 * com.baomidou.mybatisplus.extension.service.IService; import
	 * org.laokou.common.easy.excel.service.ResultService; import
	 * org.laokou.admin.server.domain.sys.entity.SysResourceDO; import
	 * org.laokou.admin.server.infrastructure.index.ResourceIndex; import
	 * org.laokou.admin.server.interfaces.qo.SysResourceQo; import
	 * org.laokou.admin.client.vo.SysResourceVO;
	 *
	 */
/**
 * @author laokou
 *//*
	 *
	 * public interface SysResourceService extends IService<SysResourceDO>,
	 * ResultService<String, ResourceIndex> {
	 *
	 */
/**
 * 分页查询资源
 * @param page
 * @param qo
 * @return
 *//*
	 *
	 * IPage<SysResourceVO> getResourceList(IPage<SysResourceVO> page, SysResourceQo qo);
	 *
	 */
/**
 * 根据id查询资源
 * @param id
 * @return
 *//*
	 *
	 * SysResourceVO getResourceById(Long id);
	 *
	 */
/**
 * 根据id删除资源
 * @param id
 *//*
	 *
	 * void deleteResource(Long id);
	 *
	 */
/**
 * 根据编码获取资源总数
 * @param code
 * @return
 *//*
	 *
	 * Long getResourceTotal(String code);
	 *
	 */
/**
 * 查询资源审批信息
 * @param id
 * @return
 *//*
	 *
	 * SysResourceVO getResourceAuditByResourceId(Long id);
	 *
	 */
/**
 * 获取版本号
 * @param id
 * @return
 *//*
	 *
	 * Integer getVersion(Long id);
	 *
	 * }
	 */
