package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysLoginLogDO;
import io.laokou.admin.domain.sys.repository.dao.SysLoginLogDao;
import io.laokou.admin.domain.sys.repository.service.SysLoginLogService;
import io.laokou.admin.interfaces.qo.LoginLogQO;
import io.laokou.admin.interfaces.vo.SysLoginLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogDao, SysLoginLogDO> implements SysLoginLogService {
    @Override
    public IPage<SysLoginLogVO> loginLogPage(IPage<SysLoginLogVO> page, LoginLogQO qo) {
        return this.baseMapper.loginLogPage(page,qo);
    }
}
