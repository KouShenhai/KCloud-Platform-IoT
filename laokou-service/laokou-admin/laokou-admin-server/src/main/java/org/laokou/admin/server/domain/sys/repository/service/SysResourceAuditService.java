package org.laokou.admin.server.domain.sys.repository.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.server.domain.sys.entity.SysResourceAuditDO;
import org.laokou.admin.server.infrastructure.processor.DsTenantProcessor;

/**
 * @author laokou
 */
@DS(DsTenantProcessor.TENANT)
public interface SysResourceAuditService extends IService<SysResourceAuditDO> {
    /**
     * 获取版本号
     * @param instanceId
     * @return
     */
    Integer getVersion(String instanceId);
}
