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

package org.laokou.admin.command.message.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.message.MessageUnreadListQry;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.admin.gatewayimpl.database.MessageMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.laokou.admin.common.Constant.TENANT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageUnreadListQryExe {

	private final MessageMapper messageMapper;

	@DS(TENANT)
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW,
			isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
	public Result<Datas<MessageCO>> execute(MessageUnreadListQry qry) {
		IPage<MessageDO> page = new Page<>(qry.getPageNum(), qry.getPageSize());
		IPage<MessageDO> newPage = messageMapper.getUnreadMessageListByUserIdAndType(page, UserUtil.getUserId(),
				qry.getType());
		long total = newPage.getTotal();
		Datas<MessageCO> datas = new Datas<>();
		datas.setTotal(total);
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), MessageCO.class));
		return Result.of(datas);
	}

}
