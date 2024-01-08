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
package org.laokou.admin.module.storage.factory;

import com.amazonaws.services.s3.AmazonS3;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.OssConvertor;
import org.laokou.admin.dto.oss.clientobject.OssCO;
import org.laokou.admin.gatewayimpl.database.OssMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OssDO;
import org.laokou.admin.module.storage.AmazonS3StorageDriver;
import org.laokou.admin.module.storage.StorageDriver;
import org.laokou.common.algorithm.template.Algorithm;
import org.laokou.common.algorithm.template.select.PollSelectAlgorithm;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.Constant.EMPTY;
import static org.laokou.common.mybatisplus.constant.DsConstant.TENANT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class StorageFactory {

	private final OssMapper ossMapper;

	private final RedisUtil redisUtil;

	private final OssConvertor ossConvertor;

	@DS(TENANT)
	public StorageDriver<AmazonS3> build(Long tenantId) {
		return new AmazonS3StorageDriver(getOssConfig(tenantId));
	}

	private OssCO getOssConfig(Long tenantId) {
		Algorithm algorithm = new PollSelectAlgorithm();
		OssDO ossDO = algorithm.select(getOssCache(tenantId), EMPTY);
		return ossConvertor.convertClientObj(ossDO);
	}

	private List<OssDO> getOssCache(Long tenantId) {
		String ossConfigKey = RedisKeyUtil.getOssConfigKey(tenantId);
		List<Object> objList = redisUtil.lGetAll(ossConfigKey);
		if (CollectionUtil.isNotEmpty(objList)) {
			return ConvertUtil.sourceToTarget(objList, OssDO.class);
		}
		else {
			List<OssDO> result = ossMapper.getOssListByFilter(EMPTY);
			if (CollectionUtil.isEmpty(result)) {
				throw new SystemException("请配置OSS");
			}
			List<Object> objs = new ArrayList<>(result);
			// 防止集群环境，其他节点新增数据
			redisUtil.delete(ossConfigKey);
			// 写入最新数据
			redisUtil.lSet(ossConfigKey, objs, RedisUtil.HOUR_ONE_EXPIRE);
			return result;
		}
	}

}
