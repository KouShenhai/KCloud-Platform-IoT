/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.laokou.admin.convertor.MessageConvertor;
import org.laokou.admin.domain.gateway.MessageGateway;
import org.laokou.admin.domain.message.Message;
import org.laokou.admin.domain.message.MessageDetail;
import org.laokou.admin.gatewayimpl.database.MessageDetailRepository;
import org.laokou.admin.gatewayimpl.database.MessageRepository;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDO;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDetailDO;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.common.MessageTypeEnum;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.laokou.common.i18n.common.RocketMqConstant.*;
import static org.laokou.common.i18n.common.TraceConstant.TRACE_ID;

/**
 * 消息管理.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageGatewayImpl implements MessageGateway {

	private final MessageRepository messageMapper;

	private final RocketMqTemplate rocketMqTemplate;

	private final TransactionalUtil transactionalUtil;

	private final MessageConvertor messageConvertor;

	private final MessageDetailRepository messageDetailMapper;

	private final MybatisUtil mybatisUtil;

	/**
	 * 新增消息.
	 * @param message 消息对象
	 */
	@Override
	public void create(Message message) {
		create(messageConvertor.toDataObject(message), message);
		rocketMqTemplate.sendAsyncMessage(LAOKOU_MESSAGE_TOPIC, getMessageTag(message.getType()),
				JacksonUtil.toJsonStr(org.laokou.common.i18n.dto.Message.builder()
					.payload(message.getDefaultMessage())
					.receiver(message.getReceiver())
					.build()),
				ThreadContext.get(TRACE_ID));
	}

	@Override
	public void read(MessageDetail messageDetail) {
		MessageDetailDO messageDetailDO = convert(messageDetail);
		messageDetailDO.setVersion(messageDetailMapper.selectVersion(messageDetailDO.getId()));
		modifyMessageDetail(messageDetailDO);
	}

	private void modifyMessageDetail(MessageDetailDO messageDetailDO) {
		transactionalUtil.defaultExecuteWithoutResult(rollback -> {
			try {
				messageDetailMapper.updateById(messageDetailDO);
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				rollback.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * 新增消息.
	 * @param messageDO 消息数据模型
	 * @param message 消息对象
	 */
	private void create(MessageDO messageDO, Message message) {
		transactionalUtil.defaultExecuteWithoutResult(rollback -> {
			try {
				messageMapper.insert(messageDO);
				createMessageDetail(messageDO.getId(), message.getReceiver());
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				rollback.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 新增消息详情.
	 * @param messageId 消息ID
	 * @param receiver 接收人列表
	 */
	private void createMessageDetail(Long messageId, Set<String> receiver) {
		List<MessageDetailDO> list = receiver.parallelStream().map(userId -> convert(messageId, userId)).toList();
		mybatisUtil.batch(list, MessageDetailRepository.class, DynamicDataSourceContextHolder.peek(),
				MessageDetailRepository::insertOne);
	}

	/**
	 * 查看消息标签.
	 * @param type 消息类型
	 * @return 消息标签
	 */
	private String getMessageTag(Integer type) {
		return type == MessageTypeEnum.NOTICE.ordinal() ? LAOKOU_NOTICE_MESSAGE_TAG : LAOKOU_REMIND_MESSAGE_TAG;
	}

	private MessageDetailDO convert(MessageDetail messageDetail) {
		MessageDetailDO messageDetailDO = new MessageDetailDO();
		messageDetailDO.setReadFlag(messageDetail.getReadFlag());
		messageDetailDO.setId(messageDetail.getId());
		return messageDetailDO;
	}

	/**
	 * 转换消息详情数据模型.
	 * @param messageId 消息ID
	 * @param userId 用户ID
	 * @return 消息详情数据模型
	 */
	private MessageDetailDO convert(Long messageId, String userId) {
		MessageDetailDO messageDetailDO = new MessageDetailDO();
		messageDetailDO.setUserId(Long.parseLong(userId));
		messageDetailDO.setId(IdGenerator.defaultSnowflakeId());
		messageDetailDO.setCreateDate(DateUtil.now());
		messageDetailDO.setCreator(UserUtil.getUserId());
		messageDetailDO.setEditor(UserUtil.getUserId());
		messageDetailDO.setDeptId(UserUtil.getDeptId());
		messageDetailDO.setTenantId(UserUtil.getTenantId());
		messageDetailDO.setMessageId(messageId);
		messageDetailDO.setDeptPath(UserUtil.getDeptPath());
		return messageDetailDO;
	}

}
