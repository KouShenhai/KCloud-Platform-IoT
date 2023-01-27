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
import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.dto.MessageDTO;
import org.laokou.admin.client.enums.ChannelTypeEnum;
import org.laokou.admin.server.application.service.SysMessageApplicationService;
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysResourceAuditDO;
import org.laokou.admin.server.domain.sys.entity.SysResourceDO;
import org.laokou.admin.server.domain.sys.repository.service.*;
import org.laokou.admin.server.infrastructure.feign.elasticsearch.ElasticsearchApiFeignClient;
import org.laokou.admin.server.infrastructure.feign.flowable.WorkTaskApiFeignClient;
import org.laokou.admin.server.infrastructure.feign.oss.OssApiFeignClient;
import org.laokou.admin.server.interfaces.qo.TaskQo;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.*;
import org.laokou.admin.client.enums.MessageTypeEnum;
import org.laokou.admin.client.dto.SysResourceAuditDTO;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.log.dto.AuditLogDTO;
import org.laokou.common.log.service.SysAuditLogService;
import org.laokou.common.log.vo.SysAuditLogVO;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.elasticsearch.client.dto.ElasticsearchDTO;
import org.laokou.elasticsearch.client.index.ResourceIndex;
import org.laokou.flowable.client.dto.AuditDTO;
import org.laokou.flowable.client.dto.ProcessDTO;
import org.laokou.flowable.client.dto.TaskDTO;
import org.laokou.flowable.client.vo.AssigneeVO;
import org.laokou.flowable.client.vo.PageVO;
import org.laokou.flowable.client.vo.TaskVO;
import org.laokou.admin.client.enums.AuditTypeEnum;
import org.laokou.redis.utils.RedisUtil;
import org.laokou.oss.client.vo.UploadVO;
import lombok.extern.slf4j.Slf4j;
import org.laokou.elasticsearch.client.dto.CreateIndexDTO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
/**
 * @author laokou
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:43
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysResourceApplicationServiceImpl implements SysResourceApplicationService {
    private static final String RESOURCE_KEY = "laokou_resource";
    private static final String PROCESS_KEY = "Process_88888888";
    private static final Integer START_AUDIT_STATUS = 1;
    private final SysResourceService sysResourceService;
    private final SysAuditLogService sysAuditLogService;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final ElasticsearchApiFeignClient elasticsearchApiFeignClient;
    private final SysMessageApplicationService sysMessageApplicationService;
    private final WorkTaskApiFeignClient workTaskApiFeignClient;
    private final OssApiFeignClient ossApiFeignClient;
    private final RedisUtil redisUtil;
    private final SysResourceAuditService sysResourceAuditService;
    @Override
    public IPage<SysResourceVO> queryResourcePage(SysResourceQo qo) {
        IPage<SysResourceVO> page = new Page(qo.getPageNum(),qo.getPageSize());
        return sysResourceService.getResourceList(page,qo);
    }

    @Override
    public Boolean syncResource(String code,String ym,String key) throws InterruptedException {
        long resourceTotal = sysResourceService.getResourceTotal(code,ym);
        if (resourceTotal == 0) {
            throw new CustomException("数据为空，无法同步数据");
        }
        // 一个小时内不能重复同步数据
        Object obj = redisUtil.get(key);
        if (obj != null) {
            throw new CustomException("数据已同步，请稍后再试");
        }
        List<String> resourceYmPartitionList;
        if (StringUtil.isNotEmpty(ym)) {
            resourceYmPartitionList = new ArrayList<>();
            resourceYmPartitionList.add(ym);
        } else {
            resourceYmPartitionList = sysResourceService.getResourceYmPartitionList(code);
        }
        String resourceIndexAlias = RESOURCE_KEY;
        String resourceIndex = RESOURCE_KEY + "_" + code;
        try {
            // 删除索引
            deleteResourceIndex(resourceYmPartitionList, resourceIndex);
            // 创建索引
            createResourceIndex(resourceYmPartitionList, resourceIndexAlias, resourceIndex);
            // 同步索引
            syncResourceIndex(code, resourceTotal, resourceIndexAlias, resourceIndex,ym);
        } catch (CustomException e) {
            // 删除redis
            throw e;
        }
        redisUtil.set(key,1,RedisUtil.HOUR_ONE_EXPIRE);
        return true;
    }

    @Override
    public SysResourceVO getResourceById(Long id) {
        return sysResourceService.getResourceById(id);
    }

    @Override
    public SysResourceVO getResourceAuditByResourceId(Long id) {
        return sysResourceService.getResourceAuditByResourceId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @GlobalTransactional
    public Boolean insertResource(SysResourceAuditDTO dto) {
        log.info("分布式事务 XID:{}", RootContext.getXID());
        SysResourceDO sysResourceDO = ConvertUtil.sourceToTarget(dto, SysResourceDO.class);
        sysResourceDO.setEditor(UserUtil.getUserId());
        sysResourceService.save(sysResourceDO);
        Long id = sysResourceDO.getId();
        String instanceId = startTask(id, sysResourceDO.getTitle());
        dto.setResourceId(id);
        return insertResourceAudit(dto,instanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @GlobalTransactional
    public Boolean updateResource(SysResourceAuditDTO dto) {
        log.info("分布式事务 XID:{}", RootContext.getXID());
        SysResourceAuditDO sysResourceAuditDO = ConvertUtil.sourceToTarget(dto, SysResourceAuditDO.class);
        sysResourceAuditDO.setEditor(UserUtil.getUserId());
        sysResourceAuditDO.setStatus(START_AUDIT_STATUS);
        String instanceId = startTask(dto.getResourceId(), dto.getTitle());
        sysResourceAuditDO.setProcessInstanceId(instanceId);
        return insertResourceAudit(dto,instanceId);
    }

    private Boolean insertResourceAudit(SysResourceAuditDTO dto,String instanceId) {
        SysResourceAuditDO sysResourceAuditDO = ConvertUtil.sourceToTarget(dto, SysResourceAuditDO.class);
        sysResourceAuditDO.setCreator(UserUtil.getUserId());
        sysResourceAuditDO.setStatus(START_AUDIT_STATUS);
        sysResourceAuditDO.setProcessInstanceId(instanceId);
        return sysResourceAuditService.save(sysResourceAuditDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteResource(Long id) {
        sysResourceService.deleteResource(id);
        return true;
    }

    @Override
    public UploadVO uploadResource(String code, MultipartFile file,String md5) {
        if (file.isEmpty()) {
            throw new CustomException("上传的文件不能为空");
        }
        //判断类型
        String fileName = file.getOriginalFilename();
        String fileSuffix = FileUtil.getFileSuffix(fileName);
        if (!FileUtil.checkFileExt(code,fileSuffix)) {
            throw new CustomException("格式不正确，请重新上传资源");
        }
        HttpResult<UploadVO> result = ossApiFeignClient.upload(file,md5);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        return result.getData();
    }

    private void syncResourceIndex(String code,long resourceTotal,final String resourceIndexAlias,final String resourceIndex,String ym) {
        beforeSync();
        int chunkSize = 500;
        int pageIndex = 0;
        while (pageIndex < resourceTotal) {
            int finalPageIndex = pageIndex;
            try {
                taskExecutor.submit(() -> {
                    List<ResourceIndex> resourceIndexList = sysResourceService.getResourceIndexList(chunkSize, finalPageIndex,code,ym);
                    Map<String, List<ResourceIndex>> resourceDataMap = resourceIndexList.stream().collect(Collectors.groupingBy(ResourceIndex::getYm));
                    for (Map.Entry<String, List<ResourceIndex>> entry : resourceDataMap.entrySet()) {
                        ElasticsearchDTO dto = new ElasticsearchDTO();
                        final String key = entry.getKey();
                        final List<ResourceIndex> resourceDataList = entry.getValue();
                        // 索引 + 时间分区
                        final String indexName = resourceIndex + "_" + key;
                        final String jsonDataList = JacksonUtil.toJsonStr(resourceDataList);
                        dto.setData(jsonDataList);
                        dto.setIndexAlias(resourceIndexAlias);
                        dto.setIndexName(indexName);
                        HttpResult<Boolean> result = elasticsearchApiFeignClient.syncBatch(dto);
                        if (!result.success()) {
                            throw new CustomException(result.getCode(),result.getMsg());
                        }
                    }
                }).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (CustomException e) {
                throw e;
            }
            pageIndex += chunkSize;
        }
        afterSync();
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

    private void createResourceIndex(List<String> resourceYmPartitionList, String resourceIndexAlias, String resourceIndex) throws InterruptedException {
        beforeCreateIndex();
        for (String ym : resourceYmPartitionList) {
            try {
                taskExecutor.submit(() -> {
                    final CreateIndexDTO dto = new CreateIndexDTO();
                    final String indexName = resourceIndex + "_" + ym;
                    dto.setIndexName(indexName);
                    dto.setIndexAlias(resourceIndexAlias);
                    HttpResult<Boolean> result = elasticsearchApiFeignClient.create(dto);
                    if (!result.success()) {
                        throw new CustomException(result.getCode(),result.getMsg());
                    }
                }).get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }  catch (CustomException e) {
                throw e;
            }
        }
        afterCreateIndex();
    }

    private void deleteResourceIndex(List<String> resourceYmPartitionList, String resourceIndex) throws InterruptedException {
        beforeDeleteIndex();
        for (String ym : resourceYmPartitionList) {
            try {
                taskExecutor.submit(() -> {
                    final String indexName = resourceIndex + "_" + ym;
                    HttpResult<Boolean> result = elasticsearchApiFeignClient.delete(indexName);
                    if (!result.success()) {
                        throw new CustomException(result.getMsg());
                    }
                }).get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }  catch (CustomException e) {
                throw e;
            }
        }
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
        log.info("分布式事务 XID:{}", RootContext.getXID());
        HttpResult<AssigneeVO> result = workTaskApiFeignClient.audit(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
        // 发送消息
        AssigneeVO vo = result.getData();
        String assignee = vo.getAssignee();
        String instanceId = vo.getInstanceId();
        Map<String, Object> values = dto.getValues();
        String instanceName = dto.getInstanceName();
        String businessKey = dto.getBusinessKey();
        Long businessId = Long.valueOf(businessKey);
        String comment = dto.getComment();
        String username = UserUtil.getUsername();
        Long userId = UserUtil.getUserId();
        int auditStatus = Integer.parseInt(values.get("auditStatus").toString());
        int status;
        //1 审核中 2 审批拒绝 3审核通过
        if (StringUtil.isNotEmpty(assignee)) {
            //审批中
            status = 1;
            insertMessage(assignee, MessageTypeEnum.REMIND.ordinal(),businessId,instanceName, ChannelTypeEnum.PLATFORM.ordinal());
        } else {
            //0拒绝 1同意
            if (0 == auditStatus) {
                //审批拒绝
                status = 2;
            } else {
                // 审批通过
                status = 3;
                // 将资源审批表的信息写入资源表
                LambdaQueryWrapper<SysResourceAuditDO> queryWrapper = Wrappers.lambdaQuery(SysResourceAuditDO.class).eq(SysResourceAuditDO::getProcessInstanceId, instanceId)
                        .eq(SysResourceAuditDO::getResourceId,businessId)
                        .select(SysResourceAuditDO::getUrl
                                , SysResourceAuditDO::getTitle
                                , SysResourceAuditDO::getRemark
                                , SysResourceAuditDO::getTags);
                SysResourceAuditDO auditDO = sysResourceAuditService.getOne(queryWrapper);
                LambdaUpdateWrapper<SysResourceDO> updateWrapper = Wrappers.lambdaUpdate(SysResourceDO.class).eq(SysResourceDO::getId, businessId)
                        .set(SysResourceDO::getTags, auditDO.getTags())
                        .set(SysResourceDO::getTitle, auditDO.getTitle())
                        .set(SysResourceDO::getRemark, auditDO.getRemark())
                        .set(SysResourceDO::getUrl, auditDO.getUrl());
                sysResourceService.update(updateWrapper);
            }
        }
        // 修改状态
        LambdaUpdateWrapper<SysResourceAuditDO> updateWrapper = Wrappers.lambdaUpdate(SysResourceAuditDO.class)
                .set(SysResourceAuditDO::getStatus, status)
                .eq(SysResourceAuditDO::getProcessInstanceId, instanceId)
                .eq(SysResourceAuditDO::getDelFlag, Constant.NO);
        sysResourceAuditService.update(updateWrapper);
        // 审核日志入队列
        saveAuditLog(businessId,auditStatus,comment,username,userId);
        return true;
    }

    private void saveAuditLog(Long businessId,int auditStatus,String comment,String username,Long userId) {
        AuditLogDTO auditLogDTO = new AuditLogDTO();
        auditLogDTO.setBusinessId(businessId);
        auditLogDTO.setAuditStatus(auditStatus);
        auditLogDTO.setAuditDate(new Date());
        auditLogDTO.setAuditName(username);
        auditLogDTO.setCreator(userId);
        auditLogDTO.setComment(comment);
        auditLogDTO.setType(AuditTypeEnum.RESOURCE.ordinal());
        sysAuditLogService.insertAuditLog(auditLogDTO);
    }

    @Override
    public IPage<TaskVO> queryResourceTask(TaskQo qo) {
        IPage<TaskVO> page = new Page<>();
        TaskDTO dto = new TaskDTO();
        dto.setPageNum(qo.getPageNum());
        dto.setPageSize(qo.getPageSize());
        dto.setUsername(UserUtil.getUsername());
        dto.setUserId(UserUtil.getUserId());
        dto.setProcessName(qo.getProcessName());
        HttpResult<PageVO<TaskVO>> result = workTaskApiFeignClient.query(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
        page.setRecords(result.getData().getRecords());
        page.setSize(dto.getPageSize());
        page.setCurrent(dto.getPageNum());
        page.setTotal(result.getData().getTotal());
        return page;
    }

    private void beforeSync() {
        log.info("开始同步数据...");
    }

    private void afterSync() {
        log.info("结束同步数据...");
    }

   private void insertMessage(String assignee, Integer type,Long id,String name,Integer sendChannel) {
        String title = "资源审批提醒";
        String content = String.format("编号为%s，名称为%s的资源需要审批，请及时查看并处理",id,name);
        Set<String> set = new HashSet<>(1);
        set.add(assignee);
        MessageDTO dto = new MessageDTO();
        dto.setContent(content);
        dto.setTitle(title);
        dto.setReceiver(set);
        dto.setType(type);
        dto.setSendChannel(sendChannel);
        sysMessageApplicationService.insertMessage(dto);
   }

    /**
     * 开始任务
     * @param businessKey 业务主键
     * @param businessName 业务名称
     * @return
     */
    private String startTask(Long businessKey,String businessName) {
        ProcessDTO dto = new ProcessDTO();
        dto.setBusinessKey(businessKey.toString());
        dto.setBusinessName(businessName);
        dto.setProcessKey(PROCESS_KEY);
        HttpResult<AssigneeVO> result = workTaskApiFeignClient.start(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
        AssigneeVO vo = result.getData();
        String instanceId = vo.getInstanceId();
        String assignee = vo.getAssignee();
        insertMessage(assignee,MessageTypeEnum.REMIND.ordinal(),businessKey,businessName, ChannelTypeEnum.PLATFORM.ordinal());
        return instanceId;
    }

}
