/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.log.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.log.entity.SysOssLogDO;
import org.laokou.common.log.event.OssLogEvent;
import org.laokou.common.log.mapper.SysOssLogMapper;
import org.laokou.common.log.service.SysOssLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laokou
 */
@Service
public class SysOssLogServiceImpl extends ServiceImpl<SysOssLogMapper, SysOssLogDO> implements SysOssLogService {

	@Override
	@DS(Constant.TENANT)
	@Transactional(rollbackFor = Exception.class)
	public Boolean insertLog(OssLogEvent event) {
		SysOssLogDO sysOssLogDO = ConvertUtil.sourceToTarget(event, SysOssLogDO.class);
		return this.baseMapper.insert(sysOssLogDO) > 0 ? true : false;
	}

	@Override
	@DS(Constant.TENANT)
	public SysOssLogDO getLogByMd5(String md5) {
		LambdaQueryWrapper<SysOssLogDO> queryWrapper = Wrappers.lambdaQuery(SysOssLogDO.class)
				.select(SysOssLogDO::getUrl).eq(SysOssLogDO::getMd5, md5);
		return this.baseMapper.selectOne(queryWrapper);
	}

}
