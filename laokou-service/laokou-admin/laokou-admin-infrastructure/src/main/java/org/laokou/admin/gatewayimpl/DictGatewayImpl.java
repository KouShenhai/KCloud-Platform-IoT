/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.BOOT_SYS_DICT;

/**
 * 字典管理.
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DictGatewayImpl implements DictGateway {

	private final DictMapper dictMapper;

	private final TransactionalUtil transactionalUtil;

	private final DictConvertor dictConvertor;

	/**
	 * 新增字典
	 * @param dict 字典对象
	 * @return 新增结果
	 */
	@Override
	public Boolean insert(Dict dict) {
		DictDO dictDO = dictConvertor.toDataObject(dict);
		return insertDict(dictDO);
	}

	/**
	 * 修改字典
	 * @param dict 字典对象
	 * @return 修改结果
	 */
	@Override
	public Boolean update(Dict dict) {
		DictDO dictDO = dictConvertor.toDataObject(dict);
		dictDO.setVersion(dictMapper.getVersion(dictDO.getId(), DictDO.class));
		return updateDict(dictDO);
	}

	/**
	 * 根据ID查看字典
	 * @param id ID
	 * @return 字典
	 */
	@Override
	public Dict getById(Long id) {
		return dictConvertor.convertEntity(dictMapper.selectById(id));
	}

	/**
	 * 根据ID删除字典
	 * @param id ID
	 * @return 删除结果
	 */
	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return dictMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 查询字典列表
	 * @param dict 字典对象
	 * @param pageQuery 分页参数
	 * @return 字典列表
	 */
	@Override
	@DataFilter(tableAlias = BOOT_SYS_DICT)
	public Datas<Dict> list(Dict dict, PageQuery pageQuery) {
		IPage<DictDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<DictDO> newPage = dictMapper.getDictListFilter(page, dict.getType(), dict.getLabel(), pageQuery);
		Datas<Dict> datas = new Datas<>();
		datas.setRecords(dictConvertor.convertEntityList(newPage.getRecords()));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

	/**
	 * 新增字典
	 * @param dictDO 字典数据模型
	 * @return 新增结果
	 */
	private Boolean insertDict(DictDO dictDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return dictMapper.insertTable(dictDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 修改字典
	 * @param dictDO 字典数据模型
	 * @return 修改结果
	 */
	private Boolean updateDict(DictDO dictDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return dictMapper.updateById(dictDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

}
