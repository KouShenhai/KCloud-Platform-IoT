/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.DictConvertor;
import org.laokou.admin.domain.dict.Dict;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 字典管理.
 *
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
	 * 新增字典.
	 * @param dict 字典对象
	 */
	@Override
	public void create(Dict dict) {
		// 验证类型和值
		long count = dictMapper.selectCount(Wrappers.lambdaQuery(DictDO.class)
			.eq(DictDO::getValue, dict.getValue())
			.eq(DictDO::getType, dict.getType()));
		dict.checkTypeAndValue(count);
		create(dictConvertor.toDataObject(dict));
	}

	/**
	 * 修改字典.
	 * @param dict 字典对象
	 */
	@Override
	public void modify(Dict dict) {
		dict.checkNullId();
		// 验证类型和值
		long count = dictMapper.selectCount(Wrappers.lambdaQuery(DictDO.class)
			.eq(DictDO::getValue, dict.getValue())
			.eq(DictDO::getType, dict.getType())
			.ne(DictDO::getId, dict.getId()));
		dict.checkTypeAndValue(count);
		DictDO dictDO = dictConvertor.toDataObject(dict);
		// 版本号
		dictDO.setVersion(dictMapper.selectVersion(dictDO.getId()));
		modify(dictDO);
	}

	/**
	 * 根据ID删除字典.
	 * @param ids IDS
	 */
	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				dictMapper.deleteBatchIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 新增字典.
	 * @param dictDO 字典数据模型
	 */
	private void create(DictDO dictDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				dictMapper.insert(dictDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 修改字典.
	 * @param dictDO 字典数据模型
	 */
	private void modify(DictDO dictDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				dictMapper.updateById(dictDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

}
