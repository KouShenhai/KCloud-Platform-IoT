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
import org.laokou.admin.common.event.DomainEventPublisher;
import org.laokou.admin.convertor.OssConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.OssGateway;
import org.laokou.admin.domain.oss.Oss;
import org.laokou.admin.domain.oss.OssLog;
import org.laokou.admin.dto.log.domainevent.OssLogEvent;
import org.laokou.admin.gatewayimpl.database.OssMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OssDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.Constant.TENANT;
import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_OSS;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssGatewayImpl implements OssGateway {

	private final OssMapper ossMapper;

	private final TransactionalUtil transactionalUtil;

	private final DomainEventPublisher domainEventPublisher;

	@Override
	@DataFilter(alias = BOOT_SYS_OSS)
	@DS(TENANT)
	public Datas<Oss> list(Oss oss, PageQuery pageQuery) {
		IPage<OssDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<OssDO> newPage = ossMapper.getOssListByLikeNameFilter(page, oss.getName(), pageQuery);
		Datas<Oss> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), Oss.class));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

	@Override
	@DS(TENANT)
	public Oss getById(Long id) {
		return ConvertUtil.sourceToTarget(ossMapper.selectById(id), Oss.class);
	}

	@Override
	@DS(TENANT)
	public Boolean insert(Oss oss) {
		OssDO ossDO = OssConvertor.toDataObject(oss);
		return insertOss(ossDO);
	}

	@Override
	public Boolean update(Oss oss) {
		OssDO ossDO = OssConvertor.toDataObject(oss);
		ossDO.setVersion(ossMapper.getVersion(ossDO.getId(), OssDO.class));
		return updateOss(ossDO);
	}

	@Override
	@DS(TENANT)
	public Boolean deleteById(Long id) {
		return transactionalUtil.execute(r -> {
			try {
				return ossMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	@Override
	public void publish(OssLog ossLog) {
		domainEventPublisher.publish(getEvent(ossLog));
	}

	private OssLogEvent getEvent(OssLog ossLog) {
		OssLogEvent event = new OssLogEvent(this);
		event.setMd5(ossLog.getMd5());
		event.setUrl(ossLog.getUrl());
		event.setName(ossLog.getName());
		event.setSize(ossLog.getSize());
		return event;
	}

	private Boolean insertOss(OssDO ossDO) {
		return transactionalUtil.execute(r -> {
			try {
				return ossMapper.insertTable(ossDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	private Boolean updateOss(OssDO ossDO) {
		return transactionalUtil.execute(r -> {
			try {
				return ossMapper.updateById(ossDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

}
