package io.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysLoginLogDO;
import io.laokou.admin.interfaces.qo.LoginLogQO;
import io.laokou.admin.interfaces.vo.SysLoginLogVO;

public interface SysLoginLogService extends IService<SysLoginLogDO> {
    IPage<SysLoginLogVO> getLoginLogList(IPage<SysLoginLogVO> page, LoginLogQO qo);
}
