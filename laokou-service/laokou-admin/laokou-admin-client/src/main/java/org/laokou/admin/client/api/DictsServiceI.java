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
package org.laokou.admin.client.api;

import org.laokou.admin.client.dto.SysDictDTO;
import org.laokou.admin.client.vo.SysDictVO;
import org.laokou.admin.server.interfaces.qo.SysDictQo;
import org.laokou.common.core.vo.OptionVO;

import java.util.List;

/**
 * @author laokou
 */
public interface DictsServiceI {

	/**
	 * 分页查询字典
	 * @param qo
	 * @return
	 */
	IPage<SysDictVO> queryDictPage(SysDictQo qo);

	/**
	 * 根据id查询字典
	 * @param id
	 * @return
	 */
	SysDictVO getDictById(Long id);

	/**
	 * 新增字典
	 * @param dto
	 * @return
	 */
	Boolean insertDict(SysDictDTO dto);

	/**
	 * 修改字典
	 * @param dto
	 * @return
	 */
	Boolean updateDict(SysDictDTO dto);

	/**
	 * 根据id删除字典
	 * @param id
	 * @return
	 */
	Boolean deleteDict(Long id);

	/**
	 * 下拉框选项
	 * @param type
	 * @return
	 */
	List<OptionVO> getOptionList(String type);

}
