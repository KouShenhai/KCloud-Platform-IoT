package org.laokou.admin.server.domain.sys.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.server.domain.sys.entity.SysOssDO;

/**
 * @author laokou
 */
public interface SysOssService extends IService<SysOssDO> {

    /**
     * 删除oss
     * @param id
     * @return
     */
    Boolean deleteOss(Long id);

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(Long id);

}
