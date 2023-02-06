package org.laokou.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.tenant.entity.SysTenantSourceDO;
import org.laokou.tenant.qo.SysTenantSourceQo;
import org.laokou.tenant.vo.SysTenantSourceVO;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface SysTenantSourceMapper extends BaseMapper<SysTenantSourceDO> {

    /**
     * 分页查询多租户数据源
     * @param qo
     * @param page
     * @return
     */
    IPage<SysTenantSourceVO> queryTenantSourcePage(IPage<SysTenantSourceVO> page, @Param("qo") SysTenantSourceQo qo);

    /**
     * 查询版本号
     * @param id
     * @return
     */
    Integer getVersion(@Param("id")Long id);

    /**
     * 根据租户id查询数据源
     * @param tenantId
     * @return
     */
    String queryTenantSourceName(@Param("tenantId") Long tenantId);


    /**
     * 查询数据库源信息
     * @param sourceName
     * @return
     */
    SysTenantSourceVO queryTenantSource(@Param("sourceName") String sourceName);

}
