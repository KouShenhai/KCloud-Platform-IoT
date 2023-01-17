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
package org.laokou.admin.server.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.server.domain.sys.entity.SysResourceDO;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysResourceVO;
import org.laokou.elasticsearch.client.index.ResourceIndex;

import java.util.List;

/**
 * @author laokou
 * @version 1.0
 * @date 2022/8/19 0019 下午 4:11
 */
public interface SysResourceService extends IService<SysResourceDO> {
    /**
     * 分页查询资源
     * @param page
     * @param qo
     * @return
     */
    IPage<SysResourceVO> getResourceList(IPage<SysResourceVO> page, SysResourceQo qo);

    /**
     * 根据id查询资源
     * @param id
     * @return
     */
    SysResourceVO getResourceById(Long id);

    /**
     * 根据id删除资源
     * @param id
     */
    void deleteResource(Long id);

    /**
     * 根据编码获取资源总数
     * @param code
     * @param ym
     * @return
     */
    Long getResourceTotal(String code,String ym);

    /**
     * 根据编码查询资源的年分区列表
     * @param code
     * @return
     */
    List<String> getResourceYmPartitionList(String code);

    /**
     * 根据偏移量查询资源列表
     * @param pageSize
     * @param pageIndex
     * @param ym
     * @param code
     * @return
     */
    List<ResourceIndex> getResourceIndexList(Integer pageSize,final Integer pageIndex, String code,String ym);
}
