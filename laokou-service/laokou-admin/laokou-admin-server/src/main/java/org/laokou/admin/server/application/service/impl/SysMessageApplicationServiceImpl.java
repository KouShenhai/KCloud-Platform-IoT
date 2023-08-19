///*
// * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//package org.laokou.admin.server.application.service.impl;
//
//import com.baomidou.dynamic.datasource.annotation.DS;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.laokou.admin.client.dto.MessageDTO;
//import org.laokou.admin.client.enums.MessageTypeEnum;
//import org.laokou.admin.client.vo.MessageDetailVO;
//import org.laokou.admin.client.vo.SysMessageVO;
//import org.laokou.admin.server.application.service.SysMessageApplicationService;
//import org.laokou.admin.server.domain.sys.entity.SysMessageDO;
//import org.laokou.admin.server.domain.sys.entity.SysMessageDetailDO;
//import org.laokou.admin.server.domain.sys.repository.service.SysMessageDetailService;
//import org.laokou.admin.server.domain.sys.repository.service.SysMessageService;
//import org.laokou.admin.server.interfaces.qo.SysMessageQo;
//import org.laokou.common.core.constant.Constant;
//import org.laokou.common.core.utils.*;
//import org.laokou.common.i18n.utils.ValidatorUtil;
//import org.laokou.common.mybatisplus.utils.BatchUtil;
//import org.laokou.common.rocketmq.constant.MqConstant;
//import org.laokou.common.rocketmq.dto.MqDTO;
//import org.laokou.common.rocketmq.template.RocketMqTemplate;
//import org.laokou.common.security.utils.UserUtil;
//import org.laokou.im.client.WsMsgDTO;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
///**
// * @author laokou
// */
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class SysMessageApplicationServiceImpl implements SysMessageApplicationService {
//
//	private final SysMessageService sysMessageService;
//
//	private final SysMessageDetailService sysMessageDetailService;
//
//	private final BatchUtil batchUtil;
//
//	private static final String DEFAULT_MESSAGE = "您有一条未读消息，请注意查收";
//
//	private final RocketMqTemplate rocketMqTemplate;
//
//	@Override
//	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
//	@DS(Constant.TENANT)
//	public Boolean insertMessage(MessageDTO dto) {
//		ValidatorUtil.validateEntity(dto);
//		SysMessageDO messageDO = ConvertUtil.sourceToTarget(dto, SysMessageDO.class);
//		messageDO.setCreateDate(DateUtil.now());
//		messageDO.setCreator(UserUtil.getUserId());
//		sysMessageService.save(messageDO);
//		Set<String> receiver = dto.getReceiver();
//		Iterator<String> iterator = receiver.iterator();
//		List<SysMessageDetailDO> detailDOList = new ArrayList<>(receiver.size());
//		while (iterator.hasNext()) {
//			String next = iterator.next();
//			SysMessageDetailDO detailDO = new SysMessageDetailDO();
//			detailDO.setMessageId(messageDO.getId());
//			detailDO.setUserId(Long.valueOf(next));
//			detailDO.setCreateDate(DateUtil.now());
//			detailDO.setCreator(UserUtil.getUserId());
//			detailDO.setId(IdGenerator.defaultSnowflakeId());
//			detailDOList.add(detailDO);
//		}
//		if (CollectionUtil.isNotEmpty(detailDOList)) {
//			// batchUtil.insertBatch(detailDOList, sysMessageDetailService::insertBatch);
//		}
//		// 推送消息
//		if (CollectionUtil.isNotEmpty(receiver)) {
//			WsMsgDTO wsMsgDTO = new WsMsgDTO();
//			wsMsgDTO.setMsg(DEFAULT_MESSAGE);
//			wsMsgDTO.setReceiver(receiver);
//			// 异步发送
//			rocketMqTemplate.sendAsyncMessage(MqConstant.LAOKOU_MESSAGE_TOPIC, getMessageTag(dto.getType()),
//					new MqDTO(JacksonUtil.toJsonStr(wsMsgDTO)));
//		}
//		return true;
//	}
//
//	@Override
//	@DS(Constant.TENANT)
//	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = true)
//	public IPage<SysMessageVO> queryMessagePage(SysMessageQo qo) {
//		ValidatorUtil.validateEntity(qo);
//		IPage<SysMessageVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
//		return sysMessageService.getMessageList(page, qo);
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	@DS(Constant.TENANT)
//	public MessageDetailVO getMessageByDetailId(Long id) {
//		final Long userId = UserUtil.getUserId();
//		Integer version = sysMessageDetailService.getVersion(id);
//		sysMessageService.readMessage(id, version, userId);
//		return sysMessageService.getMessageByDetailId(id);
//	}
//
//	@Override
//	@DS(Constant.TENANT)
//	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = true)
//	public MessageDetailVO getMessageById(Long id) {
//		return sysMessageService.getMessageById(id);
//	}
//
//	@Override
//	@DS(Constant.TENANT)
//	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = true)
//	public IPage<SysMessageVO> getUnReadList(SysMessageQo qo) {
//		IPage<SysMessageVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
//		final Long userId = UserUtil.getUserId();
//		return sysMessageService.getUnReadList(page, qo.getType(), userId);
//	}
//
//	@Override
//	@DS(Constant.TENANT)
//	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW,
//			isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
//	public Long unReadCount() {
//		final Long userId = UserUtil.getUserId();
//		return (long) sysMessageDetailService.unReadCount(userId);
//	}
//
//	private String getMessageTag(Integer type) {
//		return type == MessageTypeEnum.NOTICE.ordinal() ? MqConstant.LAOKOU_NOTICE_MESSAGE_TAG
//				: MqConstant.LAOKOU_REMIND_MESSAGE_TAG;
//	}
//
//}
