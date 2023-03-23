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
package org.laokou.admin.server.domain.sys.repository.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.admin.server.domain.sys.entity.SysResourceDO;
import org.laokou.admin.server.interfaces.qo.SysResourceQo;
import org.laokou.admin.client.vo.SysResourceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.elasticsearch.client.index.ResourceIndex;
import org.springframework.stereotype.Repository;
/**
 * @author laokou
 */
@Mapper
@Repository
public interface SysResourceMapper extends BaseMapper<SysResourceDO> {

    /**
     * 分页查询资源
     * @param page
     * @param qo
     * @return
     */
    IPage<SysResourceVO> getResourceList(IPage<SysResourceVO> page, @Param("qo") SysResourceQo qo);

    /**
     * 根据id查询资源
     * @param id
     * @return
     */
    SysResourceVO getResourceById(@Param("id") Long id);

    /**
     * 查询审批的信息
     * @param id
     * @return
     */
    SysResourceVO getResourceAuditByResourceId(@Param("id") Long id);
    /**
     * 根据编码查询资源总数
     * @param code
     * @return
     */
    Long getResourceTotal(@Param("code")String code);

    /**
     * 根据偏移量查询资源列表
     * @param code
     * @param handler
     */
    void resultList(@Param("code")String code,ResultHandler<ResourceIndex> handler);

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(@Param("id")Long id);
}
