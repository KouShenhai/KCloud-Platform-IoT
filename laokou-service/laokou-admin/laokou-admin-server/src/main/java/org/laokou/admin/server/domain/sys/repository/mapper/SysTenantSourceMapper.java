package org.laokou.admin.server.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.client.vo.SysTenantSourceVO;
import org.laokou.admin.server.domain.sys.entity.SysTenantSourceDO;
import org.laokou.admin.server.interfaces.qo.SysTenantSourceQo;
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
    IPage<SysTenantSourceVO> queryTenantSourcePage(IPage<SysTenantSourceVO> page,@Param("qo") SysTenantSourceQo qo);

    /**
     * 查询版本号
     * @param id
     * @return
     */
    Integer getVersion(@Param("id")Long id);

}
