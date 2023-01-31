package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.client.vo.SysTenantSourceVO;
import org.laokou.admin.server.interfaces.qo.SysTenantSourceQo;

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

}
