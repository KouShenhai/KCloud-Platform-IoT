package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysResourceAuditLogDO;
import io.laokou.admin.domain.sys.repository.mapper.SysResourceAuditLogMapper;
import io.laokou.admin.domain.sys.repository.service.SysResourceAuditLogService;
import io.laokou.admin.interfaces.qo.SysResourceAuditLogQO;
import io.laokou.admin.interfaces.vo.SysResourceAuditLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/26 0026 下午 5:35
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysResourceAuditLogServiceImpl extends ServiceImpl<SysResourceAuditLogMapper, SysResourceAuditLogDO> implements SysResourceAuditLogService {
    @Override
    public IPage<SysResourceAuditLogVO> getAuditLogList(IPage<SysResourceAuditLogVO> page, SysResourceAuditLogQO qo) {
        return this.baseMapper.getAuditLogList(page, qo);
    }
}
