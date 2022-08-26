package io.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysResourceAuditLogDO;
import io.laokou.admin.interfaces.qo.SysResourceAuditLogQO;
import io.laokou.admin.interfaces.vo.SysResourceAuditLogVO;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/26 0026 下午 5:35
 */
public interface SysResourceAuditLogService extends IService<SysResourceAuditLogDO> {
    IPage<SysResourceAuditLogVO> getAuditLogList(IPage<SysResourceAuditLogVO> page, SysResourceAuditLogQO qo);
}
