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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.MessageConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.MessageGateway;
import org.laokou.admin.domain.message.Message;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.MessageDetailMapper;
import org.laokou.admin.gatewayimpl.database.MessageMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDO;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDetailDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.MessageTypeEnums;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.laokou.common.i18n.common.DatasourceConstants.BOOT_SYS_MESSAGE;
import static org.laokou.common.i18n.common.RocketMqConstants.LAOKOU_NOTICE_MESSAGE_TAG;
import static org.laokou.common.i18n.common.RocketMqConstants.LAOKOU_REMIND_MESSAGE_TAG;

/**
 * 消息管理.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageGatewayImpl implements MessageGateway {

	private final MessageMapper messageMapper;

	private final RocketMqTemplate rocketMqTemplate;

	private final TransactionalUtil transactionalUtil;

	private final MessageConvertor messageConvertor;

	private final MybatisUtil mybatisUtil;

	/**
	 * 查询消息列表.
	 * @param message 消息对象
	 * @param pageQuery 分页参数
	 * @return 消息列表
	 */
	@Override
	@DataFilter(tableAlias = BOOT_SYS_MESSAGE)
	public Datas<Message> list(Message message, PageQuery pageQuery) {
		IPage<MessageDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<MessageDO> newPage = messageMapper.getMessageListFilter(page, message.getTitle(), pageQuery);
		Datas<Message> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(messageConvertor.convertEntityList(newPage.getRecords()));
		return datas;
	}

	/**
	 * 新增消息.
	 * @param message 消息对象
	 * @param user 用户对象
	 * @return 新增结果
	 */
	@Override
	public Boolean insert(Message message, User user) {
		insertMessage(messageConvertor.toDataObject(message), message, user);
		// 插入成功发送消息
		pushMessage(message.getReceiver(), message.getType());
		return true;
	}

	/**
	 * 根据ID查看消息.
	 * @param id ID
	 * @return 消息
	 */
	@Override
	public Message getById(Long id) {
		return messageConvertor.convertEntity(messageMapper.selectById(id));
	}

	/**
	 * 推送消息.
	 * @param receiver 接收人集合
	 * @param type 类型
	 */
	private void pushMessage(Set<String> receiver, Integer type) {
		if (CollectionUtil.isEmpty(receiver)) {
			return;
		}
		/*
		 * MsgCO co = new MsgCO(); co.setMsg(DEFAULT_MESSAGE); co.setReceiver(receiver);
		 */
		/*
		 * rocketMqTemplate.sendAsyncMessage(LAOKOU_MESSAGE_TOPIC, getMessageTag(type),
		 * JacksonUtil.toJsonStr(co), ThreadContext.get(TRACE_ID));
		 */
	}

	/**
	 * 新增消息.
	 * @param messageDO 消息数据模型
	 * @param message 消息对象
	 * @param user 用户对象
	 */
	private void insertMessage(MessageDO messageDO, Message message, User user) {
		transactionalUtil.defaultExecuteWithoutResult(rollback -> {
			try {
				messageMapper.insertTable(messageDO);
				insertMessageDetail(messageDO.getId(), message.getReceiver(), user);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				rollback.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 新增消息详情.
	 * @param messageId 消息ID
	 * @param receiver 接收人列表
	 * @param user 用户对象
	 */
	private void insertMessageDetail(Long messageId, Set<String> receiver, User user) {
		if (CollectionUtil.isNotEmpty(receiver)) {
			List<MessageDetailDO> list = receiver.parallelStream()
				.map(userId -> toMessageDetailDO(messageId, userId, user))
				.toList();
			mybatisUtil.batch(list, MessageDetailMapper.class, DynamicDataSourceContextHolder.peek(),
					MessageDetailMapper::save);
		}
	}

	/**
	 * 查看消息标签.
	 * @param type 消息类型
	 * @return 消息标签
	 */
	private String getMessageTag(Integer type) {
		return type == MessageTypeEnums.NOTICE.ordinal() ? LAOKOU_NOTICE_MESSAGE_TAG : LAOKOU_REMIND_MESSAGE_TAG;
	}

	/**
	 * 转换消息详情数据模型.
	 * @param messageId 消息ID
	 * @param userId 用户ID
	 * @param user 用户对象
	 * @return 消息详情数据模型
	 */
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
