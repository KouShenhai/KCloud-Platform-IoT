package org.laokou.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.tenant.entity.SysSourceDO;
import org.laokou.tenant.qo.SysSourceQo;
import org.laokou.tenant.vo.SysSourceVO;

/**
 * @author laokou
 */
public interface SysSourceService extends IService<SysSourceDO> {

    /**
     * 分页查询多租户数据源
     * @param qo
     * @param page
     * @return
     */
    IPage<SysSourceVO> querySourcePage(IPage<SysSourceVO> page, SysSourceQo qo);

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
    void deleteSource(Long id);

    /**
     * 查询数据源名称
     * @param tenantId
     * @return
     */
    String querySourceName(Long tenantId);

    /**
     * 查询数据源信息
     * @param sourceName
     * @return
     */
    SysSourceVO querySource(String sourceName);
}
