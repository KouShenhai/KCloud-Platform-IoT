/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dictItem.gateway;

import org.laokou.admin.dictItem.model.DictItemE;

/**
 * 字典项网关【防腐】.
 *
 * @author laokou
 */
public interface DictItemGateway {

	/**
	 * 新增字典项.
	 */
	void createDictItem(DictItemE dictItemE);

	/**
	 * 修改字典项.
	 */
	void updateDictItem(DictItemE dictItemE);

	/**
	 * 删除字典项.
	 */
	void deleteDictItem(Long[] ids);

	/**
	 * 字典类型是否存在.
	 */
	boolean existsDict(Long typeId);

	/**
	 * 字典项值是否存在.
	 */
	boolean existsValue(Long id, Long typeId, String value);

	/**
	 * 字典项是否存在.
	 */
	boolean existsDictItem(Long id);

	/**
	 * 字典项IDS是否存在.
	 */
	boolean existsDictItem(Long[] ids);

}
