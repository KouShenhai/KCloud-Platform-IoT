package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysOperateLogDO;
import io.laokou.admin.domain.sys.repository.dao.SysOperateLogDao;
import io.laokou.admin.domain.sys.repository.service.SysOperateLogService;
import io.laokou.admin.interfaces.qo.OperateLogQO;
import io.laokou.admin.interfaces.vo.OperateLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogDao, SysOperateLogDO> implements SysOperateLogService {
    @Override
    public IPage<OperateLogVO> operateLogPage(IPage<OperateLogVO> page,OperateLogQO qo) {
        return this.baseMapper.operateLogPage(page,qo);
    }
}
