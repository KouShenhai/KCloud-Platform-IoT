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

package org.laokou.admin.dict.gatewayimpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dict.convertor.DictConvertor;
import org.laokou.admin.dict.gateway.DictGateway;
import org.laokou.admin.dict.gatewayimpl.database.DictMapper;
import org.laokou.admin.dict.gatewayimpl.database.dataobject.DictDO;
import org.laokou.admin.dictItem.gatewayimpl.database.dataobject.DictItemDO;
import org.laokou.admin.dictItem.gatewayimpl.database.DictItemMapper;
import org.laokou.admin.dict.model.DictE;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 字典网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictGatewayImpl implements DictGateway {

	private final DictMapper dictMapper;

	private final DictItemMapper dictItemMapper;

	@Override
	public void createDict(DictE dictE) {
		dictMapper.insert(DictConvertor.toDataObject(1L, dictE));
	}

	@Override
	public void updateDict(DictE dictE) {
		DictDO dictDO = DictConvertor.toDataObject(null, dictE);
		dictDO.setVersion(dictMapper.selectVersion(dictE.getId()));
		dictMapper.updateById(dictDO);
	}

	@Override
	public void deleteDict(Long[] ids) {
		dictMapper.deleteByIds(Arrays.asList(ids));
	}

	@Override
	public boolean existsType(Long id, String type) {
		return dictMapper.selectCount(Wrappers.lambdaQuery(DictDO.class)
			.eq(DictDO::getType, type)
			.ne(ObjectUtils.isNotNull(id), DictDO::getId, id)) > 0;
	}

	@Override
	public boolean existsDict(Long id) {
		return dictMapper.selectCount(Wrappers.lambdaQuery(DictDO.class).eq(DictDO::getId, id)) > 0;
	}

	@Override
	public boolean existsDict(Long[] ids) {
		return dictMapper.selectCount(Wrappers.lambdaQuery(DictDO.class).in(DictDO::getId, Arrays.asList(ids))) == ids.length;
	}

	@Override
	public boolean existsDictItem(Long[] ids) {
		return dictItemMapper
			.selectCount(Wrappers.lambdaQuery(DictItemDO.class).in(DictItemDO::getTypeId, Arrays.asList(ids))) > 0;
	}

}
