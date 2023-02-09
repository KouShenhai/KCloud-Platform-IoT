package org.laokou.tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.tenant.entity.SysPackageMenuDO;

import java.util.List;

/**
 * @author laokou
 */
public interface SysPackageMenuService extends IService<SysPackageMenuDO> {

    /**
     * 批量插入
     * @param list
     */
    void insertBatch(List<SysPackageMenuDO> list);

}
