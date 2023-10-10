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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.SourceConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.SourceGateway;
import org.laokou.admin.domain.source.Source;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_SOURCE;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SourceGatewayImpl implements SourceGateway {

	private final SourceMapper sourceMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	@DataFilter(alias = BOOT_SYS_SOURCE)
	public Datas<Source> list(Source source, PageQuery pageQuery) {
		IPage<SourceDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<SourceDO> newPage = sourceMapper.getSourceListByLikeNameFilter(page, source.getName(), pageQuery);
		Datas<Source> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), Source.class));
		return datas;
	}

	@Override
	public Source getById(Long id) {
		SourceDO sourceDO = sourceMapper.selectById(id);
		return ConvertUtil.sourceToTarget(sourceDO, Source.class);
	}

	@Override
	public Boolean insert(Source source) {
		SourceDO sourceDO = SourceConvertor.toDataObject(source);
		return insertSource(sourceDO);
	}

	@Override
	public Boolean update(Source source) {
		SourceDO sourceDO = SourceConvertor.toDataObject(source);
		sourceDO.setVersion(sourceMapper.getVersion(sourceDO.getId(), SourceDO.class));
		return updateSource(sourceDO);
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.execute(r -> {
			try {
				return sourceMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	private Boolean updateSource(SourceDO sourceDO) {
		return transactionalUtil.execute(r -> {
			try {
				return sourceMapper.updateById(sourceDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	private Boolean insertSource(SourceDO sourceDO) {
		return transactionalUtil.execute(r -> {
			try {
				return sourceMapper.insertTable(sourceDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

}
