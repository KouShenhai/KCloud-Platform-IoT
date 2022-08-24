package io.laokou.admin.application.service.impl;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.SysMessageApplicationService;
import io.laokou.admin.domain.sys.entity.SysMessageDO;
import io.laokou.admin.domain.sys.entity.SysMessageDetailDO;
import io.laokou.admin.domain.sys.repository.service.SysMessageDetailService;
import io.laokou.admin.domain.sys.repository.service.SysMessageService;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.admin.infrastructure.component.event.SaveMessageEvent;
import io.laokou.admin.infrastructure.component.pipeline.ProcessController;
import io.laokou.admin.infrastructure.component.run.Task;
import io.laokou.admin.infrastructure.config.WebSocketServer;
import io.laokou.admin.interfaces.dto.MessageDTO;
import io.laokou.admin.interfaces.qo.MessageQO;
import io.laokou.admin.interfaces.vo.MessageDetailVO;
import io.laokou.admin.interfaces.vo.MessageVO;
import io.laokou.common.constant.Constant;
import io.laokou.common.user.SecurityUser;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.common.utils.SpringContextUtil;
import io.laokou.datasource.annotation.DataFilter;
import io.laokou.datasource.annotation.DataSource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
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
    public IPage<MessageVO> queryMessagePage(MessageQO qo) {
        IPage<MessageVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
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
    public IPage<MessageVO> getUnReadList(HttpServletRequest request, MessageQO qo) {
        IPage<MessageVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
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
