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
package org.laokou.common.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.common.easy.excel.service.ResultService;
import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.log.entity.SysLoginLogDO;
import org.laokou.common.log.event.LoginLogEvent;
import org.laokou.common.log.qo.SysLoginLogQo;
import org.laokou.common.log.vo.SysLoginLogVO;

/**
 * @author laokou
 */
public interface SysLoginLogService extends IService<SysLoginLogDO>, ResultService<SysLoginLogQo, SysLoginLogVO> {

	/**
	 * 分页查询登录日志
	 * @param page
	 * @param qo
	 * @return
	 */
	IPage<SysLoginLogVO> getLoginLogList(IPage<SysLoginLogVO> page, SysLoginLogQo qo);

	/**
	 * 新增登录日志
	 * @param event
	 * @return
	 */
	Boolean insertLoginLog(LoginLogEvent event);

	/**
	 * 导出登录日志
	 * @param qo
	 * @param response
	 */
	void exportLoginLog(SysLoginLogQo qo, HttpServletResponse response);

}
