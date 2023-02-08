package org.laokou.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.tenant.dto.SysPackageDTO;
import org.laokou.tenant.entity.SysPackageDO;
import org.laokou.tenant.qo.SysPackageQo;
import org.laokou.tenant.vo.SysPackageVO;

/**
 * @author laokou
 */
public interface SysPackageService extends IService<SysPackageDO> {

    /**
     * 套餐插入
     * @param dto
     * @return
     */
    Boolean insertPackage(SysPackageDTO dto);

    /**
     * 套餐修改
     * @param dto
     * @return
     */
    Boolean updatePackage(SysPackageDTO dto);

    /**
     * 删除套餐
     * @param id
     * @return
     */
    Boolean deletePackage(Long id);

    /**
     * 查询套餐
     * @param qo
     * @return
     */
    IPage<SysPackageVO> queryPackagePage(SysPackageQo qo);

    /**
     * 查询详情
     * @param id
     * @return
     */
    SysPackageVO getPackageById(Long id);

}
