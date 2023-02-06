package org.laokou.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.tenant.entity.SysTenantSourceDO;
import org.laokou.tenant.qo.SysTenantSourceQo;
import org.laokou.tenant.vo.SysTenantSourceVO;

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
    IPage<SysTenantSourceVO> queryTenantSourcePage(IPage<SysTenantSourceVO> page, SysTenantSourceQo qo);

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(Long id);

    /**
     * 删除多租户数据源
     * @param id
     */
    void deleteTenantSource(Long id);

    /**
     * 查询数据源名称
     * @param tenantId
     * @return
     */
    String queryTenantSourceName(Long tenantId);

    /**
     * 查询数据源信息
     * @param sourceName
     * @return
     */
    SysTenantSourceVO queryTenantSource(String sourceName);
}
