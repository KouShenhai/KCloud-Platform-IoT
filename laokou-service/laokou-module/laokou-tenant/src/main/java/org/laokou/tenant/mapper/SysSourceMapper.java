package org.laokou.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.tenant.entity.SysSourceDO;
import org.laokou.tenant.qo.SysSourceQo;
import org.laokou.tenant.vo.SysSourceVO;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface SysSourceMapper extends BaseMapper<SysSourceDO> {

    /**
     * 分页查询多租户数据源
     * @param qo
     * @param page
     * @return
     */
    IPage<SysSourceVO> querySourcePage(IPage<SysSourceVO> page, @Param("qo") SysSourceQo qo);

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
    String querySourceName(@Param("tenantId") Long tenantId);


    /**
     * 查询数据库源信息
     * @param sourceName
     * @return
     */
    SysSourceVO querySource(@Param("sourceName") String sourceName);

}
