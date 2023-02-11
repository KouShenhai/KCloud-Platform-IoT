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
import org.laokou.common.swagger.utils.ValidatorUtil;
import org.laokou.elasticsearch.client.dto.CreateIndexDTO;
import org.laokou.elasticsearch.client.dto.ElasticsearchDTO;
import org.laokou.elasticsearch.client.index.ResourceIndex;
import org.laokou.elasticsearch.client.utils.ElasticsearchFieldUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.laokou.common.core.constant.Constant.DEFAULT;

/**
 * @author laokou
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysResourceApplicationServiceImpl implements SysResourceApplicationService {
    private static final String PROCESS_KEY = "Process_88888888";
    private static final Integer START_AUDIT_STATUS = 0;
    private final SysResourceService sysResourceService;
    private final SysAuditLogService sysAuditLogService;
    private final ElasticsearchApiFeignClient elasticsearchApiFeignClient;
    private final SysMessageApplicationService sysMessageApplicationService;
    private final WorkTaskApiFeignClient workTaskApiFeignClient;
    private final OssApiFeignClient ossApiFeignClient;
    private final RedisUtil redisUtil;
    private final SysResourceAuditService sysResourceAuditService;
    @Override
    public IPage<SysResourceVO> queryResourcePage(SysResourceQo qo) {
        ValidatorUtil.validateEntity(qo);
        IPage<SysResourceVO> page = new Page(qo.getPageNum(),qo.getPageSize());
        return sysResourceService.getResourceList(page,qo);
    }

    @Override
    public Boolean syncResource(String code,String key) {
        long resourceTotal = sysResourceService.getResourceTotal(code);
        if (resourceTotal == 0) {
            throw new CustomException("数据为空，无法同步数据");
        }
        // 一个小时内不能重复同步数据
        Object obj = redisUtil.get(key);
        if (obj != null) {
            throw new CustomException("数据已同步，请稍后再试");
        }
        String indexAlias = ElasticsearchFieldUtil.RESOURCE_INDEX;
        String indexName = indexAlias + "_" + code;
        try {
            // 删除索引
            deleteResourceIndex(indexName);
            // 创建索引
            createResourceIndex(indexAlias, indexName);
            // 同步索引
            syncResourceIndex(code, indexAlias, indexName);
        } catch (CustomException e) {
            throw e;
        }
        // 删除redis
        redisUtil.set(key, DEFAULT,RedisUtil.HOUR_ONE_EXPIRE);
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
        ValidatorUtil.validateEntity(dto);
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
        ValidatorUtil.validateEntity(dto);
        log.info("分布式事务 XID:{}", RootContext.getXID());
        Long resourceId = dto.getResourceId();
        if (resourceId == null) {
            throw new CustomException("资源编号不为空");
        }
        String instanceId = startTask(resourceId, dto.getTitle());
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

    private void syncResourceIndex(String code, String indexAlias, String indexName) {
        beforeSync();
        // https://mybatis.org/mybatis-3/zh/sqlmap-xml.html
        // FORWARD_ONLY 浮标向下移动
        // 流式查询
        int chunkSize = 500;
        AtomicInteger fromIndex = new AtomicInteger(0);
        AtomicInteger toIndex = new AtomicInteger(0);
        List<ResourceIndex> list = new ArrayList<>();
        sysResourceService.handleResourceList(code, resultContext -> {
            ResourceIndex resultObject = resultContext.getResultObject();
            list.add(resultObject);
            int to = toIndex.incrementAndGet();
            if (to % chunkSize == 0) {
                int from = fromIndex.get();
                syncIndex(list.subList(from,to),indexName,indexAlias);
                fromIndex.addAndGet(chunkSize);
            }
        });
        int to = toIndex.get();
        if (to % chunkSize != 0) {
            int from = fromIndex.get();
            syncIndex(list.subList(from,to),indexName,indexAlias);
        }
        afterSync();
    }

    /**
     * 同步索引
     * @param list 数据集合
     * @param indexName 索引名称
     * @param indexAlias 索引别名
     */
    private void syncIndex(List<ResourceIndex> list,String indexName,String indexAlias) {
        ElasticsearchDTO dto = new ElasticsearchDTO();
        dto.setData(JacksonUtil.toJsonStr(list));
        dto.setIndexAlias(indexAlias);
        dto.setIndexName(indexName);
        HttpResult<Boolean> result = elasticsearchApiFeignClient.syncBatch(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
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

    private void createResourceIndex(String indexAlias, String indexName) {
        beforeCreateIndex();
        final CreateIndexDTO dto = new CreateIndexDTO();
        dto.setIndexName(indexName);
        dto.setIndexAlias(indexAlias);
        HttpResult<Boolean> result = elasticsearchApiFeignClient.create(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
        afterCreateIndex();
    }

    private void deleteResourceIndex( String resourceIndex) {
        beforeDeleteIndex();
        HttpResult<Boolean> result = elasticsearchApiFeignClient.delete(resourceIndex);
        if (!result.success()) {
            throw new CustomException(result.getMsg());
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
        ValidatorUtil.validateEntity(dto);
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
        }
        // 修改状态
        Integer version = sysResourceAuditService.getVersion(instanceId);
        LambdaUpdateWrapper<SysResourceAuditDO> updateWrapper = Wrappers.lambdaUpdate(SysResourceAuditDO.class)
                .set(SysResourceAuditDO::getStatus, status)
                .set(SysResourceAuditDO::getVersion, version + 1)
                .eq(SysResourceAuditDO::getVersion,version)
                .eq(SysResourceAuditDO::getProcessInstanceId, instanceId);
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
