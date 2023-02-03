package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.client.dto.SysTenantSourceDTO;
import org.laokou.tenant.qo.SysTenantSourceQo;
import org.laokou.tenant.vo.SysTenantSourceVO;

/**
 * @author laokou
 */
public interface SysTenantSourceApplicationService {

    /**
     * 查询多租户数据源分页
     * @param qo
     * @return
     */
    IPage<SysTenantSourceVO> queryTenantSourcePage(SysTenantSourceQo qo);

    /**
     * 新增多租户数据源
     * @param dto
     * @return
     */
    Boolean insertTenantSource(SysTenantSourceDTO dto);

    /**
     * 修改多租户数据源
     * @param dto
     * @return
     */
    Boolean updateTenantSource(SysTenantSourceDTO dto);

    /**
     * 删除多租户数据源
     * @param id
     * @return
     */
    Boolean deleteTenantSource(Long id);

    /**
     * 连接多租户数据源
     * @param dto
     * @return
     */
    Boolean connectTenantSource(SysTenantSourceDTO dto);
}
