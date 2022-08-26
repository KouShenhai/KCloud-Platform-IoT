package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.laokou.admin.application.service.SysResourceApplicationService;
import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.admin.domain.sys.entity.SysResourceDO;
import io.laokou.admin.domain.sys.repository.service.SysResourceService;
import io.laokou.admin.infrastructure.common.enums.ChannelTypeEnum;
import io.laokou.admin.infrastructure.common.enums.MessageTypeEnum;
import io.laokou.admin.infrastructure.common.utils.WorkFlowUtil;
import io.laokou.admin.interfaces.dto.SysResourceDTO;
import io.laokou.admin.interfaces.qo.SysResourceQO;
import io.laokou.admin.interfaces.vo.StartProcessVO;
import io.laokou.admin.interfaces.vo.SysResourceVO;
import io.laokou.admin.interfaces.vo.UploadVO;
import io.laokou.common.exception.CustomException;
import io.laokou.common.user.SecurityUser;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.common.utils.FileUtil;
import io.laokou.datasource.annotation.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:43
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysResourceApplicationServiceImpl implements SysResourceApplicationService {

    @Autowired
    private SysResourceService sysResourceService;

    private static final String PROCESS_KEY = "Process_88888888";

    @Autowired
    private WorkflowProcessApplicationService workflowProcessApplicationService;

    @Autowired
    private WorkFlowUtil workFlowUtil;

    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    @Autowired
    private HistoryService historyService;

    @Override
    @DataSource("master")
    public IPage<SysResourceVO> queryResourcePage(SysResourceQO qo) {
        IPage<SysResourceVO> page = new Page(qo.getPageNum(),qo.getPageSize());
        return sysResourceService.getResourceList(page,qo);
    }

    @Override
    @DataSource("master")
    public SysResourceVO getResourceById(Long id) {
        return sysResourceService.getResourceById(id);
    }

    @Override
    @DataSource("master")
    public Boolean insertResource(SysResourceDTO dto, HttpServletRequest request) {
        SysResourceDO sysResourceDO = ConvertUtil.sourceToTarget(dto, SysResourceDO.class);
        sysResourceDO.setCreator(SecurityUser.getUserId(request));
        sysResourceDO.setAuthor(SecurityUser.getUsername(request));
        sysResourceDO.setStatus(0);
        sysResourceService.save(sysResourceDO);
        String instanceId = startWork(sysResourceDO.getId(), sysResourceDO.getTitle(),request);
        sysResourceDO.setProcessInstanceId(instanceId);
        return sysResourceService.updateById(sysResourceDO);
    }

    private String startWork(Long id,String name,HttpServletRequest request) {
        StartProcessVO startProcessVO = workflowProcessApplicationService.startResourceProcess(PROCESS_KEY,id.toString(),name);
        String definitionId = startProcessVO.getDefinitionId();
        String instanceId = startProcessVO.getInstanceId();
        String auditUser = workFlowUtil.getAuditUser(definitionId, null, instanceId);
        workFlowUtil.sendAuditMsg(auditUser, MessageTypeEnum.REMIND.ordinal(), ChannelTypeEnum.PLATFORM.ordinal(),id,name,request);
        return instanceId;
    }

    @Override
    @DataSource("master")
    public Boolean updateResource(SysResourceDTO dto, HttpServletRequest request) {
        SysResourceDO sysResourceDO = ConvertUtil.sourceToTarget(dto, SysResourceDO.class);
        sysResourceDO.setEditor(SecurityUser.getUserId(request));
        sysResourceDO.setStatus(0);
        String instanceId = startWork(sysResourceDO.getId(), dto.getTitle(),request);
        sysResourceDO.setProcessInstanceId(instanceId);
        return sysResourceService.updateById(sysResourceDO);
    }

    @Override
    @DataSource("master")
    public Boolean deleteResource(Long id) {
        sysResourceService.deleteResource(id);
        return true;
    }

    @Override
    public UploadVO uploadResource(String code,String fileName, InputStream inputStream, Long fileSize) throws Exception {
        //判断类型
        String fileSuffix = FileUtil.getFileSuffix(fileName);
        if (!FileUtil.checkFileExt(code,fileSuffix)) {
            throw new CustomException("格式不正确，请重新上传资源");
        }
        UploadVO vo = new UploadVO();
        String md5 = DigestUtils.md5DigestAsHex(inputStream);
        //判断是否有md5
        LambdaQueryWrapper<SysResourceDO> wrapper = Wrappers.lambdaQuery(SysResourceDO.class);
        wrapper.eq(SysResourceDO::getMd5,md5);
        List<SysResourceDO> list = sysResourceService.list(wrapper);
        if (list.size() > 0) {
            vo.setUrl(list.get(0).getUri());
        }
        vo.setMd5(md5);
        return vo;
    }

    @Override
    public Boolean syncAsyncBatchResource(String code) {
        //总数
//        final Integer messageTotal = syncDao.getMessageTotal();
//        if (messageTotal > 0) {
//            beforeSync();
//            //创建索引 - 时间分区
//            final String messageIndex = ElasticsearchFieldUtil.MESSAGE_INDEX;
//            final List<String> messageDatePartitionList = syncDao.getMessageDatePartitionList();
//            messageDatePartitionList.stream().forEach(ym -> {
//                final CreateIndexModel model = new CreateIndexModel();
//                final String indexName = messageIndex + ElasticsearchFieldUtil.INDEX_SUFFIX + ym;
//                final String indexAlias = messageIndex;
//                model.setIndexName(indexName);
//                model.setIndexAlias(indexAlias);
//                elasticsearchApiFeignClient.crateIndex(model);
//            });
//            //同步数据 - 异步
//            final int chunkSize = ElasticsearchFieldUtil.chunkSize;
//            int pageIndex = 0;
//            while (pageIndex < messageTotal) {
//                final List<MessageIndex> messageIndexList = syncDao.getMessageIndexList(chunkSize, pageIndex);
//                final Map<String, List<MessageIndex>> messageDataMap = messageIndexList.stream().collect(Collectors.groupingBy(MessageIndex::getYm));
//                final Iterator<Map.Entry<String, List<MessageIndex>>> iterator = messageDataMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    final Map.Entry<String, List<MessageIndex>> entry = iterator.next();
//                    final String ym = entry.getKey();
//                    final List<MessageIndex> messageDataList = entry.getValue();
//                    final String indexName = messageIndex + ElasticsearchFieldUtil.INDEX_SUFFIX + ym;
//                    final String indexAlias = messageIndex;
//                    final String jsonDataList = JSON.toJSONString(messageDataList);
//                    final Integer type = SyncEnum.SYNC_ASYNC_BATCH.getValue();
//                    asyncTaskExecutor.execute(() -> new SyncElasticsearchRun(indexName,indexAlias,jsonDataList,type).start());
//                }
//                pageIndex += chunkSize;
//            }
//            afterSync();
//        }
        return true;
    }

    @Override
    public void get(String instanceId) {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).orderByHistoricActivityInstanceStartTime().desc().list();
        for (HistoricActivityInstance HistoricActivityInstance : list) {

        }
        System.out.println(list.size());
    }

    private void beforeSync() {
        log.info("开始同步数据...");
    }

    private void afterSync() {
        log.info("结束同步数据...");
    }

//    private class SyncElasticsearchRun extends Thread {
//
//        private String indexName;
//        private String indexAlias;
//        private String jsonDataList;
//        private Integer type;
//
//        SyncElasticsearchRun(String indexName,String indexAlias,String jsonDataList,Integer type) {
//            this.indexAlias = indexAlias;
//            this.indexName = indexName;
//            this.jsonDataList = jsonDataList;
//            this.type = type;
//        }
//
//        @Override
//        public void run() {
//            final ElasticsearchDTO elasticsearchDTO = new ElasticsearchDTO();
//            final MqDTO mqDTO = new MqDTO();
//            elasticsearchDTO.setIndexName(indexName);
//            elasticsearchDTO.setData(jsonDataList);
//            elasticsearchDTO.setType(type);
//            elasticsearchDTO.setIndexAlias(indexAlias);
//            mqDTO.setData(JSON.toJSONString(elasticsearchDTO));
//            //同步数据
//        }
//    }

}
