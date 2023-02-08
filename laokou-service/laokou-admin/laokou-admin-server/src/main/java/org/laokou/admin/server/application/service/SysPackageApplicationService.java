package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.tenant.dto.SysPackageDTO;
import org.laokou.tenant.qo.SysPackageQo;
import org.laokou.tenant.vo.SysPackageVO;

/**
 * @author laokou
 */
public interface SysPackageApplicationService {

    /**
     * 新增套餐
     * @param dto
     * @return
     */
    Boolean insertPackage(SysPackageDTO dto);

    /**
     * 修改套餐
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
     * 分页查询
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
