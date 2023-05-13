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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.laokou.admin.client.dto.MessageDTO;
import org.laokou.admin.client.enums.AuditEnum;
import org.laokou.admin.client.enums.AuditStatusEnum;
import org.laokou.admin.server.application.service.SysMessageApplicationService;
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysResourceAuditDO;
import org.laokou.admin.server.domain.sys.entity.SysResourceDO;
import org.laokou.admin.server.domain.sys.repository.service.*;
import org.laokou.admin.server.infrastructure.feign.workflow.WorkTaskApiFeignClient;
import org.laokou.admin.server.infrastructure.index.ResourceIndex;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.oss.support.OssTemplate;
import org.laokou.admin.server.interfaces.qo.TaskQo;
import org.laokou.common.core.utils.*;
import org.laokou.admin.client.enums.MessageTypeEnum;
import org.laokou.admin.client.dto.SysResourceAuditDTO;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.log.event.AuditLogEvent;
import org.laokou.common.log.service.SysAuditLogService;
import org.laokou.common.log.vo.SysAuditLogVO;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.oss.vo.UploadVO;
import org.laokou.flowable.client.dto.*;
import org.laokou.flowable.client.vo.AssigneeVO;
import org.laokou.flowable.client.vo.PageVO;
import org.laokou.flowable.client.vo.TaskVO;
import org.laokou.admin.client.enums.AuditTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
/**
 * @author laokou
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysResourceApplicationServiceImpl implements SysResourceApplicationService {
    private static final String PROCESS_KEY = "Process_88888888";
    private static final String AUDIT_STATUS = "auditStatus";
    private final SysResourceService sysResourceService;
    private final SysAuditLogService sysAuditLogService;
    private final SysMessageApplicationService sysMessageApplicationService;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final WorkTaskApiFeignClient workTaskApiFeignClient;
    private final OssTemplate ossTemplate;
    private final SysResourceAuditService sysResourceAuditService;
    private static final String RESOURCE_INDEX = "laokou_resource";
    @Override
    public IPage<SysResourceVO> queryResourcePage(SysResourceQo qo) {
        ValidatorUtil.validateEntity(qo);
        IPage<SysResourceVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        return sysResourceService.getResourceList(page,qo);
    }

    @Override
    public Boolean syncResource(String code,String key) {
        long resourceTotal = sysResourceService.getResourceTotal(code);
        if (resourceTotal == 0) {
            throw new CustomException("数据为空，无法同步数据");
        }
        String indexAlias = RESOURCE_INDEX;
        String indexName = indexAlias + "_" + code;
        // 删除索引
        deleteResourceIndex(indexName);
        // 创建索引
        createResourceIndex(indexAlias, indexName);
        // 同步索引
        syncResourceIndex(code, indexName);
        return true;
    }

    @Override
    public SysResourceVO getResourceById(Long id) {
        return sysResourceService.getResourceById(id);
    }

    @Override
    public void downLoadResource(Long id, HttpServletResponse response) throws IOException {
        SysResourceVO resource = sysResourceService.getResourceById(id);
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + resource.getTitle());
        URL url = new URL(resource.getUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(6000);
        try (InputStream inputStream = conn.getInputStream();
             ServletOutputStream outputStream = response.getOutputStream()) {
            IOUtils.copy(inputStream,outputStream);
            conn.disconnect();
        }
    }

    @Override
    public SysResourceVO getResourceAuditByResourceId(Long id) {
        return sysResourceService.getResourceAuditByResourceId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @GlobalTransactional
    public Boolean insertResource(SysResourceAuditDTO dto) {
        ValidatorUtil.validateEntity(dto);
        log.info("分布式事务 XID:{}", RootContext.getXID());
        SysResourceDO sysResourceDO = ConvertUtil.sourceToTarget(dto, SysResourceDO.class);
        sysResourceDO.setEditor(UserUtil.getUserId());
        sysResourceService.save(sysResourceDO);
        Long id = sysResourceDO.getId();
        // 开启任务
        return insertAuditMessage(startTask(id, dto.getTitle()), dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @GlobalTransactional
    public Boolean updateResource(SysResourceAuditDTO dto) {
        ValidatorUtil.validateEntity(dto);
        log.info("分布式事务 XID:{}", RootContext.getXID());
        Long resourceId = dto.getResourceId();
        if (resourceId == null) {
            throw new CustomException("资源编号不为空");
        }
        // 开启任务
        return insertAuditMessage(startTask(resourceId, dto.getTitle()), dto);
    }

    private void insertResourceAudit(SysResourceAuditDTO dto, String instanceId) {
        SysResourceAuditDO sysResourceAuditDO = ConvertUtil.sourceToTarget(dto, SysResourceAuditDO.class);
        sysResourceAuditDO.setCreator(UserUtil.getUserId());
        sysResourceAuditDO.setStatus(AuditStatusEnum.INIT.ordinal());
        sysResourceAuditDO.setProcessInstanceId(instanceId);
        sysResourceAuditService.save(sysResourceAuditDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteResource(Long id) {
        sysResourceService.deleteResource(id);
        return true;
    }

    @Override
    @SneakyThrows
    public UploadVO uploadResource(String code, MultipartFile file, String md5) {
        if (file.isEmpty()) {
            throw new CustomException("上传的文件不能为空");
        }
        InputStream inputStream = file.getInputStream();
        long size = file.getSize();
        String contentType = file.getContentType();
        // 判断类型
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileExt = FileUtil.getFileExt(fileName);
        if (StringUtil.isNotEmpty(code) && !FileUtil.checkFileExt(code,fileExt)) {
            throw new CustomException("格式不正确，请重新上传资源");
        }
        return ossTemplate.upload(size,md5,fileName,contentType,inputStream);
    }

    @SneakyThrows
    private void syncResourceIndex(String code, String indexName) {
        beforeSync();
        // https://mybatis.org/mybatis-3/zh/sqlmap-xml.html
        // FORWARD_ONLY 浮标向下移动
        // 流式查询
        int chunkSize = 500;
        List<ResourceIndex> list = Collections.synchronizedList(new ArrayList<>(chunkSize));
        sysResourceService.resultList(code, resultContext -> {
            ResourceIndex resultObject = resultContext.getResultObject();
            list.add(resultObject);
            if (list.size() % chunkSize == 0) {
                syncIndex(list,indexName);
            }
        });
        if (list.size() % chunkSize != 0) {
            syncIndex(list,indexName);
        }
        afterSync();
    }

    private Boolean insertAuditMessage(AssigneeVO vo,SysResourceAuditDTO dto) {
        String instanceId = vo.getInstanceId();
        String assignee = vo.getAssignee();
        Long resourceId = dto.getResourceId();
        String title = dto.getTitle();
        insertResourceAudit(dto,instanceId);
        insertAuditMessage(assignee,resourceId,title);
        return true;
    }

    /**
     * 同步索引
     * @param list 数据集合
     * @param indexName 索引名称
     */
    @SneakyThrows
    private void syncIndex(List<ResourceIndex> list,String indexName) {
        elasticsearchTemplate.syncBatchIndex(indexName,JacksonUtil.toJsonStr(list));
        // 清除list
        list.clear();
    }

    @Override
    public List<SysAuditLogVO>   queryAuditLogList(Long businessId) {
        return sysAuditLogService.getAuditLogList(businessId,AuditTypeEnum.RESOURCE.ordinal());
    }

    private void beforeCreateIndex() {
        log.info("开始索引创建...");
    }

    private void afterCreateIndex() {
        log.info("结束索引创建...");
    }

    @SneakyThrows
    private void createResourceIndex(String indexAlias, String indexName) {
        beforeCreateIndex();
        elasticsearchTemplate.createIndex(indexName,indexAlias, ResourceIndex.class);
        afterCreateIndex();
    }

    @SneakyThrows
    private void deleteResourceIndex( String resourceIndex) {
        beforeDeleteIndex();
        elasticsearchTemplate.deleteIndex(resourceIndex);
        afterDeleteIndex();
    }

    private void beforeDeleteIndex() {
        log.info("开始索引删除...");
    }

    private void afterDeleteIndex() {
        log.info("结束索引删除...");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public Boolean auditResourceTask(AuditDTO dto) {
        ValidatorUtil.validateEntity(dto);
        log.info("分布式事务 XID:{}", RootContext.getXID());
        HttpResult<AssigneeVO> result = workTaskApiFeignClient.audit(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        AssigneeVO vo = result.getData();
        String assignee = vo.getAssignee();
        String instanceId = vo.getInstanceId();
        Map<String, Object> values = dto.getValues();
        String instanceName = dto.getInstanceName();
        String businessKey = dto.getBusinessKey();
        Long businessId = Long.valueOf(businessKey);
        String comment = dto.getComment();
        String username = UserUtil.getUserName();
        Long userId = UserUtil.getUserId();
        int auditStatus = Integer.parseInt(values.get(AUDIT_STATUS).toString());
        int status;
        boolean isAudit = false;
        //1 审核中 2 审批拒绝 3审核通过
        if (StringUtil.isNotEmpty(assignee)) {
            // 审批中
            status = AuditStatusEnum.AUDIT.ordinal();
        } else {
            // auditStatus => 0拒绝 1同意
            if (AuditEnum.NO.ordinal() == auditStatus) {
                //审批拒绝
                status = AuditStatusEnum.REJECT.ordinal();
            } else {
                // 审批通过
                status = AuditStatusEnum.AGREE.ordinal();
            }
        }
        switch (AuditStatusEnum.getStatus(status)) {
            // 审批中,发送审批消息通知
            case AUDIT -> isAudit = true;
            // 审批通过
            case AGREE -> auditAgree(businessId, instanceId);
            default -> {}
        }
        // 修改审批状态
        updateAuditStatus(status, instanceId);
        // 审核日志
        insertAuditLog(businessId, auditStatus, comment, username, userId);
        // 审批消息
        if (isAudit) {
            insertAuditMessage(assignee, businessId, instanceName);
        }
        return true;
    }

    /**
     * 审批通过
     * @param businessId 实例ID
     */
    private void auditAgree(Long businessId,String instanceId) {
        // 将资源审批表的信息写入资源表
        LambdaQueryWrapper<SysResourceAuditDO> queryWrapper = Wrappers.lambdaQuery(SysResourceAuditDO.class).eq(SysResourceAuditDO::getProcessInstanceId, instanceId)
                .eq(SysResourceAuditDO::getResourceId,businessId)
                .select(SysResourceAuditDO::getUrl
                        , SysResourceAuditDO::getTitle
                        , SysResourceAuditDO::getRemark);
        SysResourceAuditDO auditDO = sysResourceAuditService.getOne(queryWrapper);
        Integer version = sysResourceService.getVersion(businessId);
        LambdaUpdateWrapper<SysResourceDO> updateWrapper = Wrappers.lambdaUpdate(SysResourceDO.class).eq(SysResourceDO::getId, businessId)
                .eq(SysResourceDO::getVersion,version)
                .set(SysResourceDO::getVersion, version + 1)
                .set(SysResourceDO::getTitle, auditDO.getTitle())
                .set(SysResourceDO::getRemark, auditDO.getRemark())
                .set(SysResourceDO::getUrl, auditDO.getUrl());
        sysResourceService.update(updateWrapper);
    }

    /**
     * 修改审批状态
     * @param status 状态
     * @param instanceId 实例ID
     */
    private void updateAuditStatus(int status,String instanceId) {
        // 修改状态
        Integer version = sysResourceAuditService.getVersion(instanceId);
        LambdaUpdateWrapper<SysResourceAuditDO> updateWrapper = Wrappers.lambdaUpdate(SysResourceAuditDO.class)
                .set(SysResourceAuditDO::getStatus, status)
                .set(SysResourceAuditDO::getVersion, version + 1)
                .eq(SysResourceAuditDO::getVersion,version)
                .eq(SysResourceAuditDO::getProcessInstanceId, instanceId);
        sysResourceAuditService.update(updateWrapper);
    }

    private void insertAuditLog(Long businessId,int auditStatus,String comment,String username,Long userId) {
        AuditLogEvent auditLogEvent = new AuditLogEvent(this);
        auditLogEvent.setBusinessId(businessId);
        auditLogEvent.setAuditStatus(auditStatus);
        auditLogEvent.setAuditDate(DateUtil.now());
        auditLogEvent.setAuditName(username);
        auditLogEvent.setCreator(userId);
        auditLogEvent.setComment(comment);
        auditLogEvent.setType(AuditTypeEnum.RESOURCE.ordinal());
        SpringContextUtil.publishEvent(auditLogEvent);
    }

    @Override
    public IPage<TaskVO> queryResourceTask(TaskQo qo) {
        IPage<TaskVO> page = new Page<>();
        TaskDTO dto = new TaskDTO();
        dto.setPageNum(qo.getPageNum());
        dto.setPageSize(qo.getPageSize());
        dto.setUsername(UserUtil.getUserName());
        dto.setUserId(UserUtil.getUserId());
        dto.setProcessName(qo.getProcessName());
        dto.setProcessKey(PROCESS_KEY);
        HttpResult<PageVO<TaskVO>> result = workTaskApiFeignClient.query(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
        PageVO<TaskVO> taskPageVO = Optional.ofNullable(result.getData()).orElseGet(PageVO::new);
        page.setRecords(taskPageVO.getRecords());
        page.setSize(dto.getPageSize());
        page.setCurrent(dto.getPageNum());
        page.setTotal(Optional.ofNullable(taskPageVO.getTotal()).orElse(0L));
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public Boolean resolveResourceTask(ResolveDTO dto) {
        HttpResult<AssigneeVO> result = workTaskApiFeignClient.resolve(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        // 发送通知
        AssigneeVO vo = result.getData();
        String assignee = vo.getAssignee();
        Long businessId = Long.valueOf(dto.getBusinessKey());
        String instanceName = dto.getInstanceName();
        insertAuditMessage(assignee, businessId, instanceName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public Boolean transferResourceTask(TransferDTO dto) {
        HttpResult<AssigneeVO> result = workTaskApiFeignClient.transfer(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        // 发送通知
        AssigneeVO vo = result.getData();
        String assignee = vo.getAssignee();
        Long businessId = Long.valueOf(dto.getBusinessKey());
        String instanceName = dto.getInstanceName();
        insertAuditMessage(assignee, businessId, instanceName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public Boolean delegateResourceTask(DelegateDTO dto) {
        HttpResult<AssigneeVO> result = workTaskApiFeignClient.delegate(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        // 发送通知
        AssigneeVO vo = result.getData();
        String assignee = vo.getAssignee();
        Long businessId = Long.valueOf(dto.getBusinessKey());
        String instanceName = dto.getInstanceName();
        insertResolveMessage(assignee, businessId, instanceName);
        return true;
    }

    private void beforeSync() {
        log.info("开始同步数据...");
    }

    private void afterSync() {
        log.info("结束同步数据...");
    }

    private void insertMessage(String assignee,String title,String content) {
        Set<String> set = new HashSet<>(1);
        set.add(assignee);
        MessageDTO dto = new MessageDTO();
        dto.setContent(content);
        dto.setTitle(title);
        dto.setReceiver(set);
        dto.setType(MessageTypeEnum.REMIND.ordinal());
        sysMessageApplicationService.insertMessage(dto);
    }

    @Async
   public void insertAuditMessage(String assignee,Long id,String name) {
        String title = "资源审批任务提醒";
        String content = String.format("编号为%s，名称为%s的资源需要审批，请及时查看并审批",id,name);
        insertMessage(assignee,title,content);
   }

   @Async
   public void insertResolveMessage(String assignee,Long id,String name) {
        String title = "资源处理任务提醒";
        String content = String.format("编号为%s，名称为%s的资源需要处理，请及时查看并处理",id,name);
        insertMessage(assignee,title,content);
   }

    /**
     * 开始任务
     * @param businessKey 业务主键
     * @param businessName 业务名称
     * @return 返回实例编号
     */
    private AssigneeVO startTask(Long businessKey,String businessName) {
        ProcessDTO dto = new ProcessDTO();
        dto.setBusinessKey(businessKey.toString());
        dto.setBusinessName(businessName);
        dto.setProcessKey(PROCESS_KEY);
        HttpResult<AssigneeVO> result = workTaskApiFeignClient.start(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
        return result.getData();
    }

}
