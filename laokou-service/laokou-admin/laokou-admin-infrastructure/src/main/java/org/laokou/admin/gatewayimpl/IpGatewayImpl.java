/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.IpConvertor;
import org.laokou.admin.domain.gateway.IpGateway;
import org.laokou.admin.domain.ip.Ip;
import org.laokou.admin.gatewayimpl.database.IpMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.IpDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.laokou.common.i18n.common.Constant.DEFAULT;
import static org.laokou.common.i18n.common.Constant.STAR;
import static org.laokou.common.redis.utils.RedisUtil.NOT_EXPIRE;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IpGatewayImpl implements IpGateway {

	private final IpMapper ipMapper;

	private final IpConvertor ipConvertor;

	private final TransactionalUtil transactionalUtil;

	private final RedisUtil redisUtil;

	@Override
	public Boolean insert(Ip ip) {
		return insertIp(ipConvertor.toDataObject(ip));
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return ipMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	@Override
	public Datas<Ip> list(Ip ip, PageQuery pageQuery) {
		IPage<IpDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<IpDO> newPage = ipMapper.selectPage(page,
				Wrappers.lambdaQuery(IpDO.class).eq(IpDO::getLabel, ip.getLabel()).select(IpDO::getId, IpDO::getValue));
		Datas<Ip> datas = new Datas<>();
		datas.setRecords(ipConvertor.convertEntityList(newPage.getRecords()));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

	@Override
	public Boolean refresh(Ip ip) {
		String label = ip.getLabel();
		List<IpDO> list = ipMapper
			.selectList(Wrappers.lambdaQuery(IpDO.class).eq(IpDO::getLabel, label).select(IpDO::getValue));
		if (CollectionUtil.isEmpty(list)) {
			return false;
		}
		Set<String> keys = redisUtil.keys(RedisKeyUtil.getIpCacheKey(label, STAR));
		if (CollectionUtil.isNotEmpty(keys)) {
			redisUtil.delete(keys.toArray(String[]::new));
		}
		list.forEach(item -> {
			String ipCacheKey = RedisKeyUtil.getIpCacheKey(label, item.getValue());
			redisUtil.set(ipCacheKey, DEFAULT, NOT_EXPIRE);
		});
		return true;
	}

	private Boolean insertIp(IpDO ipDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return ipMapper.insertTable(ipDO);
			}
			catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

}
