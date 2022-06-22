package io.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysLoginLogDO;
import io.laokou.admin.interfaces.qo.LoginLogQO;
import io.laokou.admin.interfaces.vo.LoginLogVO;

public interface SysLoginLogService extends IService<SysLoginLogDO> {
    IPage<LoginLogVO> loginLogPage(IPage<LoginLogVO> page, LoginLogQO qo);
}
