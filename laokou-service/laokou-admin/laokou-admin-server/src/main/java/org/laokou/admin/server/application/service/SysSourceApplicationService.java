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
	 * package org.laokou.admin.server.application.service;
	 *
	 * import com.baomidou.mybatisplus.core.metadata.IPage; import
	 * org.laokou.common.core.vo.OptionVO; import
	 * org.laokou.common.tenant.dto.SysSourceDTO; import
	 * org.laokou.common.tenant.qo.SysSourceQo; import
	 * org.laokou.common.tenant.vo.SysSourceVO;
	 *
	 * import java.util.List;
	 *
	 */
/**
 * @author laokou
 *//*
	 *
	 * public interface SysSourceApplicationService {
	 *
	 */
/**
 * 查询多租户数据源分页
 * @param qo
 * @return
 *//*
	 *
	 * IPage<SysSourceVO> querySourcePage(SysSourceQo qo);
	 *
	 */
/**
 * 新增多租户数据源
 * @param dto
 * @return
 *//*
	 *
	 * Boolean insertSource(SysSourceDTO dto);
	 *
	 */
/**
 * 修改多租户数据源
 * @param dto
 * @return
 *//*
	 *
	 * Boolean updateSource(SysSourceDTO dto);
	 *
	 */
/**
 * 删除多租户数据源
 * @param id
 * @return
 *//*
	 *
	 * Boolean deleteSource(Long id);
	 *
	 */
/**
 * 数据源详情
 * @param id
 * @return
 *//*
	 *
	 * SysSourceVO getSourceById(Long id);
	 *
	 */
/**
 * 获取下拉框
 * @return
 *//*
	 *
	 * List<OptionVO> getOptionList();
	 *
	 * }
	 */
