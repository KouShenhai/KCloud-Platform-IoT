package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

}
