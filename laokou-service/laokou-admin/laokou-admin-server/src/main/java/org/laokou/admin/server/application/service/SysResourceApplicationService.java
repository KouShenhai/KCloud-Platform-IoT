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
package org.laokou.admin.server.application.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.client.dto.SysResourceAuditDTO;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.admin.server.interfaces.qo.TaskQo;
import org.laokou.common.log.vo.SysAuditLogVO;
import org.laokou.flowable.client.dto.AuditDTO;
import org.laokou.flowable.client.vo.TaskVO;
import org.laokou.oss.client.vo.UploadVO;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
/**
 * @author laokou
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:42
 */
public interface SysResourceApplicationService {

    /**
     * 分页查询资源
     * @param qo
     * @return
     */
    IPage<SysResourceVO> queryResourcePage(SysResourceQo qo);

    /**
     * 全量同步数据
     * @param code
     * @param key
     * @return
     * @throws InterruptedException
     */
    Boolean syncResource(String code,String key) throws InterruptedException;

    /**
     * 根据id查询资源
     * @param id
     * @return
     */
    SysResourceVO getResourceById(Long id);

    /**
     * 查询资源审批信息
     * @param id
     * @return
     */
    SysResourceVO getResourceAuditByResourceId(Long id);

    /**
     * 新增资源
     * @param dto
     * @throws IOException
     * @return
     */
    Boolean insertResource(SysResourceAuditDTO dto) throws IOException;

    /**
     * 修改资源
     * @param dto
     * @throws IOException
     * @return
     */
    Boolean updateResource(SysResourceAuditDTO dto) throws IOException;

    /**
     * 根据id删除资源
     * @param id
     * @return
     */
    Boolean deleteResource(Long id);

    /**
     * 上传资源
     * @param code
     * @param file
     * @param md5
     * @return
     * @throws Exception
     */
    UploadVO uploadResource(String code, MultipartFile file,String md5) throws Exception;

    /**
     * 查询资源审核日志列表
     * @param businessId
     * @return
     */
    List<SysAuditLogVO> queryAuditLogList(Long businessId);

    /**
     * 资源审批
     * @param dto
     * @return
     */
    Boolean auditResourceTask(AuditDTO dto);

    /**
     * 任务列表
     * @param qo
     * @return
     */
    IPage<TaskVO> queryResourceTask(TaskQo qo);

}
