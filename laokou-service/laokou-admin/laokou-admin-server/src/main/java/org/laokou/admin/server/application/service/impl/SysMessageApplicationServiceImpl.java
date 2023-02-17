/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.application.service.impl;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.client.enums.ChannelTypeEnum;
import org.laokou.admin.server.application.service.SysMessageApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysMessageDO;
import org.laokou.admin.server.domain.sys.entity.SysMessageDetailDO;
import org.laokou.admin.server.domain.sys.repository.service.SysMessageDetailService;
import org.laokou.admin.server.domain.sys.repository.service.SysMessageService;
import org.laokou.admin.client.dto.MessageDTO;
import org.laokou.admin.server.infrastructure.feign.im.ImApiFeignClient;
import org.laokou.admin.server.interfaces.qo.SysMessageQo;
import org.laokou.admin.client.vo.MessageDetailVO;
import org.laokou.admin.client.vo.SysMessageVO;
import org.apache.commons.collections.CollectionUtils;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.exception.CustomException;
import org.laokou.common.core.utils.HttpResult;
import org.laokou.common.core.utils.ValidatorUtil;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.im.client.PushMsgDTO;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.redis.utils.RedisUtil;
import org.laokou.tenant.processor.DsTenantProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * @author laokou
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysMessageApplicationServiceImpl implements SysMessageApplicationService {

    private final SysMessageService sysMessageService;

    private final SysMessageDetailService sysMessageDetailService;
    private final ImApiFeignClient imApiFeignClient;
    private final RedisUtil redisUtil;
    private final BatchUtil<SysMessageDetailDO> batchUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DS(DsTenantProcessor.TENANT)
    public Boolean insertMessage(MessageDTO dto) {
        ValidatorUtil.validateEntity(dto);
        SysMessageDO messageDO = ConvertUtil.sourceToTarget(dto, SysMessageDO.class);
        Integer sendChannel = dto.getSendChannel();
        messageDO.setCreateDate(new Date());
        messageDO.setCreator(UserUtil.getUserId());
        messageDO.setSendChannel(sendChannel);
        sysMessageService.save(messageDO);
        if (ChannelTypeEnum.PLATFORM.ordinal() == sendChannel) {
            Set<String> receiver = dto.getReceiver();
            Iterator<String> iterator = receiver.iterator();
            List<SysMessageDetailDO> detailDOList = new ArrayList<>(receiver.size());
            while (iterator.hasNext()) {
                String next = iterator.next();
                SysMessageDetailDO detailDO = new SysMessageDetailDO();
                detailDO.setMessageId(messageDO.getId());
                detailDO.setUserId(Long.valueOf(next));
                detailDO.setCreateDate(new Date());
                detailDO.setCreator(UserUtil.getUserId());
                detailDOList.add(detailDO);
            }
            if (CollectionUtils.isNotEmpty(detailDOList)) {
                if (detailDOList.size() != 1) {
                    batchUtil.insertConcurrentBatch(detailDOList,500,sysMessageDetailService);
                } else {
                    sysMessageDetailService.save(detailDOList.stream().findFirst().get());
                }
            }
            // 平台-发送消息
            if (CollectionUtils.isNotEmpty(receiver)) {
                PushMsgDTO pushMsgDTO = new PushMsgDTO();
                pushMsgDTO.setMsg("您有一条未读消息，请注意查收");
                pushMsgDTO.setReceiver(receiver);
                HttpResult<Boolean> result = imApiFeignClient.push(pushMsgDTO);
                if (!result.success()) {
                    throw new CustomException(result.getCode(), result.getMsg());
                } else {
                    receiver.forEach(item -> {
                        // 根据用户，分别将递增未读消息数
                        String messageUnReadKey = RedisKeyUtil.getMessageUnReadKey(Long.valueOf(item));
                        Object obj = redisUtil.get(messageUnReadKey);
                        if (obj != null) {
                            redisUtil.incrementAndGet(messageUnReadKey);
                        }
                    });
                }
            }
        }
        return true;
    }

    @Override
    @DS(DsTenantProcessor.TENANT)
    public IPage<SysMessageVO> queryMessagePage(SysMessageQo qo) {
        ValidatorUtil.validateEntity(qo);
        IPage<SysMessageVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysMessageService.getMessageList(page,qo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DS(DsTenantProcessor.TENANT)
    public MessageDetailVO getMessageByDetailId(Long id) {
        Integer version = sysMessageDetailService.getVersion(id);
        sysMessageService.readMessage(id,version);
        final Long userId = UserUtil.getUserId();
        String messageUnReadKey = RedisKeyUtil.getMessageUnReadKey(userId);
        Object obj = redisUtil.get(messageUnReadKey);
        if (obj != null) {
            redisUtil.decrementAndGet(messageUnReadKey);
        }
        return sysMessageService.getMessageByDetailId(id);
    }

    @Override
    @DS(DsTenantProcessor.TENANT)
    public MessageDetailVO getMessageById(Long id) {
        return sysMessageService.getMessageById(id);
    }

    @Override
    @DS(DsTenantProcessor.TENANT)
    public IPage<SysMessageVO> getUnReadList(SysMessageQo qo) {
        IPage<SysMessageVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        final Long userId = UserUtil.getUserId();
        return sysMessageService.getUnReadList(page,qo.getType(),userId);
    }

    @Override
    @DS(DsTenantProcessor.TENANT)
    public Long unReadCount() {
        final Long userId = UserUtil.getUserId();
        String messageUnReadKey = RedisKeyUtil.getMessageUnReadKey(userId);
        Object obj = redisUtil.get(messageUnReadKey);
        if (obj != null) {
            return Long.valueOf("" + obj);
        }
        long count = sysMessageDetailService.messageCount(userId);
        redisUtil.addAndGet(messageUnReadKey,count);
        return count;
    }

}
