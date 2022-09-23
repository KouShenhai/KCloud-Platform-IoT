/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.admin.application.service.impl;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.laokou.admin.application.service.SysMessageApplicationService;
import org.laokou.admin.domain.sys.entity.SysMessageDO;
import org.laokou.admin.domain.sys.entity.SysMessageDetailDO;
import org.laokou.admin.domain.sys.repository.service.SysMessageDetailService;
import org.laokou.admin.domain.sys.repository.service.SysMessageService;
import org.laokou.admin.domain.sys.repository.service.SysUserService;
import org.laokou.admin.infrastructure.component.event.SaveMessageEvent;
import org.laokou.admin.infrastructure.component.pipeline.ProcessController;
import org.laokou.admin.infrastructure.component.run.Task;
import org.laokou.admin.infrastructure.config.WebSocketServer;
import org.laokou.admin.interfaces.dto.MessageDTO;
import org.laokou.admin.interfaces.qo.SysMessageQO;
import org.laokou.admin.interfaces.vo.MessageDetailVO;
import org.laokou.admin.interfaces.vo.SysMessageVO;
import org.laokou.common.constant.Constant;
import org.laokou.common.user.SecurityUser;
import org.laokou.common.user.UserDetail;
import org.laokou.common.utils.ConvertUtil;
import org.laokou.common.utils.SpringContextUtil;
import org.laokou.datasource.annotation.DataFilter;
import org.laokou.datasource.annotation.DataSource;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
@Service
@GlobalTransactional(rollbackFor = Exception.class)
public class SysMessageApplicationServiceImpl implements SysMessageApplicationService {

    public static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(
            8,
            16,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(512),
            ThreadUtil.newNamedThreadFactory("laokou-message-service",true),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private ProcessController processController;

    @Autowired
    private SysMessageService sysMessageService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMessageDetailService sysMessageDetailService;

    @Override
    public Boolean pushMessage(MessageDTO dto) throws IOException {
        Iterator<String> iterator = dto.getReceiver().iterator();
        while (iterator.hasNext()) {
            webSocketServer.sendMessages(String.format("%s发来一条消息",dto.getUsername()),Long.valueOf(iterator.next()));
        }
        return true;
    }

    @Override
    public Boolean sendMessage(MessageDTO dto, HttpServletRequest request) {
        String username = dto.getUsername();
        Long userId = dto.getUserId();
        dto.setUsername(null == username ? SecurityUser.getUsername(request) : username);
        dto.setUserId(null == userId ? SecurityUser.getUserId(request) : userId);
        processController.process(dto);
        return true;
    }

    @Override
    public void consumeMessage(MessageDTO dto) {
        //1.插入日志
        SpringContextUtil.publishEvent(new SaveMessageEvent(dto));
        //2.推送消息
        Task task = SpringContextUtil.getBean(Task.class).setDto(dto);
        executorService.execute(task);
    }

    @Override
    @DataSource("master")
    public Boolean insertMessage(MessageDTO dto) {
        SysMessageDO messageDO = ConvertUtil.sourceToTarget(dto, SysMessageDO.class);
        messageDO.setCreateDate(new Date());
        messageDO.setCreator(dto.getUserId());
        final UserDetail userDetail = sysUserService.getUserDetail(messageDO.getCreator());
        messageDO.setDeptId(userDetail.getDeptId());
        sysMessageService.save(messageDO);
        Iterator<String> iterator = dto.getReceiver().iterator();
        List<SysMessageDetailDO> detailDOList = Lists.newArrayList();
        while (iterator.hasNext()) {
            String next = iterator.next();
            SysMessageDetailDO detailDO = new SysMessageDetailDO();
            detailDO.setMessageId(messageDO.getId());
            detailDO.setUserId(Long.valueOf(next));
            detailDO.setCreateDate(new Date());
            detailDO.setCreator(dto.getUserId());
            detailDOList.add(detailDO);
        }
        if (CollectionUtils.isNotEmpty(detailDOList)) {
            sysMessageDetailService.saveBatch(detailDOList);
        }
        return true;
    }

    @Override
    @DataSource("master")
    @DataFilter(tableAlias = "boot_sys_message")
    public IPage<SysMessageVO> queryMessagePage(SysMessageQO qo) {
        IPage<SysMessageVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysMessageService.getMessageList(page,qo);
    }

    @Override
    @DataSource("master")
    public MessageDetailVO getMessageByDetailId(Long id) {
        sysMessageService.readMessage(id);
        return sysMessageService.getMessageByDetailId(id);
    }

    @Override
    @DataSource("master")
    public MessageDetailVO getMessageById(Long id) {
        return sysMessageService.getMessageById(id);
    }

    @Override
    @DataSource("master")
    public IPage<SysMessageVO> getUnReadList(HttpServletRequest request, SysMessageQO qo) {
        IPage<SysMessageVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        final Long userId = SecurityUser.getUserId(request);
        return sysMessageService.getUnReadList(page,userId);
    }

    @Override
    @DataSource("master")
    public Integer unReadCount(HttpServletRequest request) {
        final Long userId = SecurityUser.getUserId(request);
        return sysMessageDetailService.count(Wrappers.lambdaQuery(SysMessageDetailDO.class).eq(SysMessageDetailDO::getUserId,userId)
                .eq(SysMessageDetailDO::getDelFlag,Constant.NO)
                .eq(SysMessageDetailDO::getReadFlag, Constant.NO));
    }

}
