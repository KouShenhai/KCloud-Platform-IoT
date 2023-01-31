package org.laokou.admin.server.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.client.vo.SysTenantSourceVO;
import org.laokou.admin.server.domain.sys.entity.SysTenantSourceDO;
import org.laokou.admin.server.interfaces.qo.SysTenantSourceQo;

/**
 * @author laokou
 */
public interface SysTenantSourceService extends IService<SysTenantSourceDO> {

    /**
     * 分页查询多租户数据源
     * @param qo
     * @param page
     * @return
     */
    IPage<SysTenantSourceVO> queryTenantSourcePage(IPage<SysTenantSourceVO> page,SysTenantSourceQo qo);

}
