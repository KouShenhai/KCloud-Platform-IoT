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
 *//*

package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.tenant.dto.SysPackageDTO;
import org.laokou.common.tenant.qo.SysPackageQo;
import org.laokou.common.tenant.vo.SysPackageVO;

import java.util.List;

*/
/**
 * @author laokou
 *//*

public interface SysPackageApplicationService {

	*/
/**
	 * 新增套餐
	 * @param dto
	 * @return
	 *//*

	Boolean insertPackage(SysPackageDTO dto);

	*/
/**
	 * 修改套餐
	 * @param dto
	 * @return
	 *//*

	Boolean updatePackage(SysPackageDTO dto);

	*/
/**
	 * 删除套餐
	 * @param id
	 * @return
	 *//*

	Boolean deletePackage(Long id);

	*/
/**
	 * 分页查询
	 * @param qo
	 * @return
	 *//*

	IPage<SysPackageVO> queryPackagePage(SysPackageQo qo);

	*/
/**
	 * 查询详情
	 * @param id
	 * @return
	 *//*

	SysPackageVO getPackageById(Long id);

	*/
/**
	 * 下拉选择框
	 * @return
	 *//*

	List<OptionVO> getOptionList();

}
*/
