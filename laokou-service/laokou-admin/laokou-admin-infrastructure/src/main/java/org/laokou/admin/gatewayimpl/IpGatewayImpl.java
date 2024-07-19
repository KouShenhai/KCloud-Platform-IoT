/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.convertor.IpConvertor;
import org.laokou.admin.domain.gateway.IpGateway;
import org.laokou.admin.domain.ip.Ip;
import org.laokou.admin.gatewayimpl.database.IpMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.IpDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.laokou.common.mybatisplus.mapper.BaseDO.DEFAULT_TENANT_ID;
import static org.laokou.common.redis.utils.RedisUtil.NOT_EXPIRE;

/**
 * IP管理.
 *
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

	/**
	 * 新增IP.
	 *
	 * @param ip IP对象
	 */
	@Override
	public void create(Ip ip) {
		create(ipConvertor.toDataObject(ip));
	}

	/**
	 * 根据IDS删除IP.
	 *
	 * @param ids IDS
	 */
	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				ipMapper.deleteByIds(Arrays.asList(ids));
			} catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * 刷新IP至Redis.
	 *
	 * @param ip IP对象
	 */
	@Override
	public void refresh(Ip ip) {
		String label = ip.getLabel();
		List<IpDO> list = ipMapper
			.selectList(Wrappers.lambdaQuery(IpDO.class).eq(IpDO::getLabel, label).select(IpDO::getValue));
		if (CollectionUtil.isEmpty(list)) {
			return;
		}
		String ipCacheHashKey = RedisKeyUtil.getIpCacheHashKey(label);
		redisUtil.hDel(ipCacheHashKey);
		redisUtil.hSet(ipCacheHashKey,
			list.stream().collect(Collectors.toMap(IpDO::getValue, val -> DEFAULT_TENANT_ID)), NOT_EXPIRE);
	}

	/**
	 * 新增IP.
	 *
	 * @param ipDO IP数据模型
	 */
	private void create(IpDO ipDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				ipMapper.insert(ipDO);
			} catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

}
