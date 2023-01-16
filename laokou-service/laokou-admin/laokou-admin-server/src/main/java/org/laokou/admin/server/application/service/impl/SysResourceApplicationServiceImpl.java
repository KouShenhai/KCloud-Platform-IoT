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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.dto.MessageDTO;
import org.laokou.admin.server.application.service.SysMessageApplicationService;
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysResourceDO;
import org.laokou.admin.server.domain.sys.repository.service.SysAuditLogService;
import org.laokou.admin.server.domain.sys.repository.service.SysResourceService;
import org.laokou.admin.server.infrastructure.feign.flowable.WorkTaskApiFeignClient;
import org.laokou.admin.server.infrastructure.feign.oss.OssApiFeignClient;
import org.laokou.admin.server.interfaces.qo.TaskQo;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.*;
import org.laokou.admin.client.enums.MessageTypeEnum;
import org.laokou.admin.server.infrastructure.feign.rocketmq.RocketmqApiFeignClient;
import org.laokou.admin.client.dto.SysResourceDTO;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysAuditLogVO;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.elasticsearch.client.index.ResourceIndex;
import org.laokou.flowable.client.dto.AuditDTO;
import org.laokou.flowable.client.dto.ProcessDTO;
import org.laokou.flowable.client.dto.TaskDTO;
import org.laokou.flowable.client.vo.AssigneeVO;
import org.laokou.flowable.client.vo.PageVO;
import org.laokou.flowable.client.vo.TaskVO;
import org.laokou.log.client.dto.AuditLogDTO;
import org.laokou.log.client.dto.enums.AuditTypeEnum;
import org.laokou.rocketmq.client.dto.RocketmqDTO;
import org.laokou.oss.client.vo.UploadVO;
import lombok.extern.slf4j.Slf4j;
import org.laokou.elasticsearch.client.dto.CreateIndexDTO;
import org.laokou.rocketmq.client.constant.RocketmqConstant;
import org.laokou.rocketmq.client.dto.SyncIndexDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author laokou
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:43
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysResourceApplicationServiceImpl implements SysResourceApplicationService {
    private static final String RESOURCE_KEY = "laokou_resource";
    private static final String PROCESS_KEY = "Process_88888888";
    private static final Integer INIT_STATUS = 0;
    private final SysResourceService sysResourceService;
    private final SysAuditLogService sysAuditLogService;
    private final RocketmqApiFeignClient rocketmqApiFeignClient;
    private final SysMessageApplicationService sysMessageApplicationService;
    private final WorkTaskApiFeignClient workTaskApiFeignClient;
    private final OssApiFeignClient ossApiFeignClient;

    @Override
    public IPage<SysResourceVO> queryResourcePage(SysResourceQo qo) {
        IPage<SysResourceVO> page = new Page(qo.getPageNum(),qo.getPageSize());
        return sysResourceService.getResourceList(page,qo);
    }

    @Override
    public SysResourceVO getResourceById(Long id) {
        return sysResourceService.getResourceById(id);
    }

    @Override
    public Boolean insertResource(SysResourceDTO dto) {
        SysResourceDO sysResourceDO = ConvertUtil.sourceToTarget(dto, SysResourceDO.class);
        sysResourceDO.setCreator(UserUtil.getUserId());
        sysResourceDO.setAuthor(UserUtil.getUsername());
        sysResourceDO.setStatus(INIT_STATUS);
        sysResourceService.save(sysResourceDO);
        String instanceId = startTask(sysResourceDO.getId(), sysResourceDO.getTitle());
        sysResourceDO.setProcessInstanceId(instanceId);
        return sysResourceService.updateById(sysResourceDO);
    }

    @Override
    public Boolean updateResource(SysResourceDTO dto) {
        SysResourceDO sysResourceDO = ConvertUtil.sourceToTarget(dto, SysResourceDO.class);
        sysResourceDO.setEditor(UserUtil.getUserId());
        sysResourceDO.setStatus(INIT_STATUS);
        String instanceId = startTask(sysResourceDO.getId(), dto.getTitle());
        sysResourceDO.setProcessInstanceId(instanceId);
        return sysResourceService.updateById(sysResourceDO);
    }

    @Override
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

    @Override
    public Boolean syncResourceIndex(String code) {
        //总数
        final Long resourceTotal = sysResourceService.getResourceTotal(code);
        if (resourceTotal > 0) {
            beforeSyncAsync();
            //创建索引 - 时间分区
            final String resourceIndex = RESOURCE_KEY + "_" + code;
            //同步数据 - 异步
            final int chunkSize = 500;
            int pageIndex = 0;
            while (pageIndex < resourceTotal) {
                final List<ResourceIndex> resourceIndexList = sysResourceService.getResourceIndexList(chunkSize, pageIndex,code);
                final Map<String, List<ResourceIndex>> resourceDataMap = resourceIndexList.stream().collect(Collectors.groupingBy(ResourceIndex::getYm));
                for (Map.Entry<String, List<ResourceIndex>> entry : resourceDataMap.entrySet()) {
                    final String ym = entry.getKey();
                    final List<ResourceIndex> resourceDataList = entry.getValue();
                    //同步数据
                    try {
                        RocketmqDTO dto = new RocketmqDTO();
                        final String indexName = resourceIndex + "_" + ym;
                        final String jsonDataList = JacksonUtil.toJsonStr(resourceDataList);
                        final SyncIndexDTO syncIndexDTO = new SyncIndexDTO();
                        syncIndexDTO.setIndexName(indexName);
                        syncIndexDTO.setData(jsonDataList);
                        dto.setData(JacksonUtil.toJsonStr(syncIndexDTO));
                        rocketmqApiFeignClient.sendMessage(RocketmqConstant.LAOKOU_SYNC_INDEX_TOPIC,dto);
                    } catch (final FeignException e) {
                        log.error("错误信息：{}",e.getMessage());
                    }
                }
                pageIndex += chunkSize;
            }
            afterSyncAsync();
        }
        return true;
    }

    @Override
    public List<SysAuditLogVO> queryAuditLogList(Long businessId) {
        return sysAuditLogService.getAuditLogList(businessId,AuditTypeEnum.RESOURCE.ordinal());
    }

    private void beforeCreateIndex() {
        log.info("开始索引创建...");
    }

    private void afterCreateIndex() {
        log.info("结束索引创建...");
    }

    @Override
    public Boolean createResourceIndex(String code) {
        // 总数
        final Long resourceTotal = sysResourceService.getResourceTotal(code);
        if (resourceTotal > 0) {
            beforeCreateIndex();
            //创建索引 - 时间分区
            final String resourceIndex = RESOURCE_KEY + "_" + code;
            final String resourceIndexAlias = RESOURCE_KEY;
            final List<String> resourceYmPartitionList = sysResourceService.getResourceYmPartitionList(code);
            for (String ym : resourceYmPartitionList) {
                try {
                    RocketmqDTO rocketmqDTO = new RocketmqDTO();
                    final CreateIndexDTO dto = new CreateIndexDTO();
                    final String indexName = resourceIndex + "_" + ym;
                    dto.setIndexName(indexName);
                    dto.setIndexAlias(resourceIndexAlias);
                    rocketmqDTO.setData(JacksonUtil.toJsonStr(dto));
                    rocketmqApiFeignClient.sendMessage(RocketmqConstant.LAOKOU_CREATE_INDEX_TOPIC, rocketmqDTO);
                } catch (final FeignException e) {
                    log.error("错误信息：{}", e.getMessage());
                }
            }
            afterCreateIndex();
        }
        return true;
    }

    @Override
    public Boolean deleteResourceIndex(String code) {
        // 总数
        final Long resourceTotal = sysResourceService.getResourceTotal(code);
        if (resourceTotal > 0) {
            beforeDeleteIndex();
            //创建索引 - 时间分区
            final String resourceIndex = RESOURCE_KEY + "_" + code;
            final List<String> resourceYmPartitionList = sysResourceService.getResourceYmPartitionList(code);
            for (String ym : resourceYmPartitionList) {
                try {
                    RocketmqDTO rocketmqDTO = new RocketmqDTO();
                    final String indexName = resourceIndex + "_" + ym;
                    rocketmqDTO.setData(indexName);
                    rocketmqApiFeignClient.sendMessage(RocketmqConstant.LAOKOU_DELETE_INDEX_TOPIC, rocketmqDTO);
                } catch (final FeignException e) {
                    log.error("错误信息：{}", e.getMessage());
                }
            }
            afterDeleteIndex();
        }
        return true;
    }

    private void beforeDeleteIndex() {
        log.info("开始索引删除...");
    }

    private void afterDeleteIndex() {
        log.info("结束索引删除...");
    }

    @Override
    public Boolean auditResourceTask(AuditDTO dto) {
        try {
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
                insertMessage(assignee, MessageTypeEnum.REMIND.ordinal(),businessId,instanceName);
            } else {
                //0拒绝 1同意
                if (0 == auditStatus) {
                    //审批拒绝
                    status = 2;
                } else {
                    //审批通过
                    status = 3;
                }
            }
            // 修改状态
            LambdaUpdateWrapper<SysResourceDO> updateWrapper = Wrappers.lambdaUpdate(SysResourceDO.class)
                    .set(SysResourceDO::getStatus, status)
                    .eq(SysResourceDO::getProcessInstanceId, instanceId)
                    .eq(SysResourceDO::getDelFlag, Constant.NO);
            sysResourceService.update(updateWrapper);
            // 审核日志入队列
           saveAuditLog(businessId,auditStatus,comment,username,userId);
        } catch (FeignException e) {
            log.error("错误信息：{}",e.getMessage());
            throw new CustomException("未启动流程，请联系管理员");
        }
        return true;
    }

    private void saveAuditLog(Long businessId,int auditStatus,String comment,String username,Long userId) {
        try {
            AuditLogDTO auditLogDTO = new AuditLogDTO();
            auditLogDTO.setBusinessId(businessId);
            auditLogDTO.setAuditStatus(auditStatus);
            auditLogDTO.setAuditDate(new Date());
            auditLogDTO.setAuditName(username);
            auditLogDTO.setCreator(userId);
            auditLogDTO.setComment(comment);
            auditLogDTO.setType(AuditTypeEnum.RESOURCE.ordinal());
            RocketmqDTO rocketmqDTO = new RocketmqDTO();
            rocketmqDTO.setData(JacksonUtil.toJsonStr(auditLogDTO));
            rocketmqApiFeignClient.sendOneMessage(RocketmqConstant.LAOKOU_AUDIT_LOG_TOPIC, rocketmqDTO);
        } catch (FeignException e) {
            log.error("错误信息：{}",e.getMessage());
        }
    }

    @Override
    public IPage<TaskVO> queryResourceTask(TaskQo qo) {
        IPage<TaskVO> page = new Page<>();
        try {
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
        } catch (FeignException e) {
            log.error("报错信息：{}",e.getMessage());
        }
        return page;
    }

    private void beforeSyncAsync() {
        log.info("开始异步同步数据...");
    }

    private void afterSyncAsync() {
        log.info("结束异步同步数据...");
    }

   private void insertMessage(String assignee, Integer type,Long id,String name) {
        String title = "资源审批提醒";
        String content = String.format("编号为%s，名称为%s的资源需要审批，请及时查看并处理",id,name);
        Set<String> set = new HashSet<>(1);
        set.add(assignee);
        MessageDTO dto = new MessageDTO();
        dto.setContent(content);
        dto.setTitle(title);
        dto.setPlatformReceiver(set);
        dto.setType(type);
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
        insertMessage(assignee,MessageTypeEnum.REMIND.ordinal(),businessKey,businessName);
        return instanceId;
    }

}
