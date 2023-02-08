package org.laokou.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.tenant.entity.SysPackageDO;
import org.laokou.tenant.qo.SysPackageQo;
import org.laokou.tenant.vo.SysPackageVO;

/**
 * @author laokou
 */
public interface SysPackageService extends IService<SysPackageDO> {

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(Long id);

    /**
     * 删除套餐
     * @param id
     * @return
     */
    Boolean deletePackage(Long id);

    /**
     * 查询套餐
     * @param qo
     * @param page
     * @return
     */
    IPage<SysPackageVO> queryPackagePage(IPage<SysPackageVO> page,SysPackageQo qo);

    /**
     * 查询详情
     * @param id
     * @return
     */
    SysPackageVO getPackageById(Long id);

}
