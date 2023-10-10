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

package org.laokou.admin.gatewayimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.DictConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.dict.Dict;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.Constant.TENANT;
import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_DICT;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DictGatewayImpl implements DictGateway {

	private final DictMapper dictMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public Boolean insert(Dict dict) {
		DictDO dictDO = DictConvertor.toDataObject(dict);
		return insertDict(dictDO);
	}

	@Override
	public Boolean update(Dict dict) {
		DictDO dictDO = DictConvertor.toDataObject(dict);
		dictDO.setVersion(dictMapper.getVersion(dictDO.getId(), DictDO.class));
		return updateDict(dictDO);
	}

	@Override
	@DS(TENANT)
	public Dict getById(Long id) {
		DictDO dictDO = dictMapper.selectById(id);
		return ConvertUtil.sourceToTarget(dictDO, Dict.class);
	}

	@Override
	@DS(TENANT)
	public Boolean deleteById(Long id) {
		return transactionalUtil.execute(r -> {
			try {
				return dictMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	@Override
	@DataFilter(alias = BOOT_SYS_DICT)
	@DS(TENANT)
	public Datas<Dict> list(Dict dict, PageQuery pageQuery) {
		IPage<DictDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<DictDO> newPage = dictMapper.getDictListByLikeTypeAndLikeLabelFilter(page, dict.getType(),
				dict.getLabel(), pageQuery);
		Datas<Dict> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), Dict.class));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

	private Boolean insertDict(DictDO dictDO) {
		return transactionalUtil.execute(r -> {
			try {
				return dictMapper.insertTable(dictDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	private Boolean updateDict(DictDO dictDO) {
		return transactionalUtil.execute(r -> {
			try {
				return dictMapper.updateById(dictDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

}
