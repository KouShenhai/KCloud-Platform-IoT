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
package org.laokou.common.log.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.easy.excel.suppert.ExcelTemplate;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.log.event.LoginLogEvent;
import org.laokou.common.log.entity.SysLoginLogDO;
import org.laokou.common.log.excel.SysLoginLogExcel;
import org.laokou.common.log.mapper.SysLoginLogMapper;
import org.laokou.common.log.qo.SysLoginLogQo;
import org.laokou.common.log.service.SysLoginLogService;
import org.laokou.common.log.vo.SysLoginLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLogDO>
		implements SysLoginLogService {

	private final ExcelTemplate<SysLoginLogQo, SysLoginLogVO> excelTemplate;

	@Override
	@DS(Constant.SHARDING_SPHERE_READWRITE)
	public IPage<SysLoginLogVO> getLoginLogList(IPage<SysLoginLogVO> page, SysLoginLogQo qo) {
		return this.baseMapper.getLoginLogList(page, qo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@DS(Constant.SHARDING_SPHERE_READWRITE)
	public Boolean insertLoginLog(LoginLogEvent event) {
		SysLoginLogDO logDO = ConvertUtil.sourceToTarget(event, SysLoginLogDO.class);
		return baseMapper.insert(logDO) > 0 ? true : false;
	}

	@Override
	public void exportLoginLog(SysLoginLogQo qo, HttpServletResponse response) {
		excelTemplate.export(500, response, qo, this, SysLoginLogExcel.class);
	}

	@Override
	public void resultList(SysLoginLogQo qo, ResultHandler<SysLoginLogVO> resultHandler) {
		this.baseMapper.resultList(qo, resultHandler);
	}

}
