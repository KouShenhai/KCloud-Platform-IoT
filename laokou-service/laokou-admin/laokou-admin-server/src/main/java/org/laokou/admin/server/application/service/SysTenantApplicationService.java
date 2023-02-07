package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.tenant.dto.SysTenantDTO;
import org.laokou.tenant.qo.SysTenantQo;
import org.laokou.tenant.vo.SysTenantVO;

/**
 * @author laokou
 */
public interface SysTenantApplicationService {

    /**
     * 分页查询租户
     * @param qo
     * @return
     */
    IPage<SysTenantVO> queryTenantPage(SysTenantQo qo);

    /**
     * 新增租户
     * @param dto
     * @return
     */
    Boolean insertTenant(SysTenantDTO dto);

    /**
     * 查询租户
     * @param id
     * @return
     */
    SysTenantVO getTenantById(Long id);

    /**
     * 修改租户
     * @param dto
     * @return
     */
    Boolean updateTenant(SysTenantDTO dto);

    /**
     * 删除租户
     * @param id
     * @return
     */
    Boolean deleteTenant(Long id);
}
