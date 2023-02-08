package org.laokou.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.tenant.entity.SysPackageDO;
import org.laokou.tenant.qo.SysPackageQo;
import org.laokou.tenant.vo.SysPackageVO;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface SysPackageMapper extends BaseMapper<SysPackageDO> {

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(Long id);

    /**
     * 获取详情
     * @param id
     * @return
     */
    SysPackageVO getPackageById(Long id);

    /**
     * 查询套餐
     * @param page
     * @param qo
     * @return
     */
    IPage<SysPackageVO> queryPackagePage(IPage<SysPackageVO> page,@Param("qo") SysPackageQo qo);

}
