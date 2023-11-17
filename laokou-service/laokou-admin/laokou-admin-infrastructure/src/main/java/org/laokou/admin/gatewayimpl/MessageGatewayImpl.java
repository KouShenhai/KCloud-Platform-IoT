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

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.laokou.admin.convertor.MessageConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.MessageGateway;
import org.laokou.admin.domain.message.Message;
import org.laokou.admin.domain.message.Type;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.MessageDetailMapper;
import org.laokou.admin.gatewayimpl.database.MessageMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDO;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDetailDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.laokou.im.dto.message.clientobject.WsMsgCO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.laokou.common.i18n.common.Constant.TRACE_ID;
import static org.laokou.common.mybatisplus.constant.DsConstant.BOOT_SYS_MESSAGE;
import static org.laokou.common.mybatisplus.constant.DsConstant.TENANT;
import static org.laokou.common.rocketmq.constant.MqConstant.*;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageGatewayImpl implements MessageGateway {

	private final MessageMapper messageMapper;

	private static final String DEFAULT_MESSAGE = "您有一条未读消息，请注意查收";

	private final RocketMqTemplate rocketMqTemplate;

	private final TransactionalUtil transactionalUtil;

	private final MessageConvertor messageConvertor;

	private final BatchUtil batchUtil;

	@Override
	@DataFilter(alias = BOOT_SYS_MESSAGE)
	@DS(TENANT)
	public Datas<Message> list(Message message, PageQuery pageQuery) {
		IPage<MessageDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<MessageDO> newPage = messageMapper.getMessageListFilter(page, message.getTitle(), pageQuery);
		Datas<Message> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(messageConvertor.convertEntityList(newPage.getRecords()));
		return datas;
	}

	@Override
	@DS(TENANT)
	public Boolean insert(Message message, User user) {
		insertMessage(messageConvertor.toDataObject(message), message, user);
		// 插入成功发送消息
		pushMessage(message.getReceiver(), message.getType());
		return true;
	}

	@Override
	@DS(TENANT)
	public Message getById(Long id) {
		return messageConvertor.convertEntity(messageMapper.selectById(id));
	}

	private void pushMessage(Set<String> receiver, Integer type) {
		if (CollectionUtil.isEmpty(receiver)) {
			return;
		}
		WsMsgCO co = new WsMsgCO();
		co.setMsg(DEFAULT_MESSAGE);
		co.setReceiver(receiver);
		rocketMqTemplate.sendAsyncMessage(LAOKOU_MESSAGE_TOPIC, getMessageTag(type), JacksonUtil.toJsonStr(co),
				ThreadContext.get(TRACE_ID));
	}

	private void insertMessage(MessageDO messageDO, Message message, User user) {
		transactionalUtil.executeWithoutResult(rollback -> {
			try {
				messageMapper.insertTable(messageDO);
				insertMessageDetail(messageDO.getId(), message.getReceiver(), user);
			}
			catch (Exception e) {
				log.error("错误信息", e);
				rollback.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private void insertMessageDetail(Long messageId, Set<String> receiver, User user) {
		if (CollectionUtil.isNotEmpty(receiver)) {
			List<MessageDetailDO> list = receiver.parallelStream()
				.map(userId -> toMessageDetailDO(messageId, userId, user))
				.toList();
			batchUtil.insertBatch(list, MessageDetailMapper.class, DynamicDataSourceContextHolder.peek());
		}
	}

	private String getMessageTag(Integer type) {
		return type == Type.NOTICE.ordinal() ? LAOKOU_NOTICE_MESSAGE_TAG : LAOKOU_REMIND_MESSAGE_TAG;
	}

	private MessageDetailDO toMessageDetailDO(Long messageId, String userId, User user) {
		MessageDetailDO messageDetailDO = new MessageDetailDO();
		messageDetailDO.setUserId(Long.parseLong(userId));
		messageDetailDO.setId(IdGenerator.defaultSnowflakeId());
		messageDetailDO.setCreateDate(DateUtil.now());
		messageDetailDO.setCreator(user.getId());
		messageDetailDO.setDeptId(user.getDeptId());
		messageDetailDO.setTenantId(user.getTenantId());
		messageDetailDO.setMessageId(messageId);
		messageDetailDO.setDeptPath(user.getDeptPath());
		return messageDetailDO;
	}

}
