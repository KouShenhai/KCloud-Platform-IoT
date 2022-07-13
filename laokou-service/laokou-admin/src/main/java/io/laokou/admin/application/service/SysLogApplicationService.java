package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.qo.LoginLogQO;
import io.laokou.admin.interfaces.qo.SysOperateLogQO;
import io.laokou.admin.interfaces.vo.SysLoginLogVO;
import io.laokou.admin.interfaces.vo.SysOperateLogVO;
import io.laokou.common.dto.LoginLogDTO;
import io.laokou.common.dto.OperateLogDTO;

public interface SysLogApplicationService {

    Boolean insertOperateLog(OperateLogDTO dto);

    Boolean insertLoginLog(LoginLogDTO dto);

    IPage<SysOperateLogVO> queryOperateLogPage(SysOperateLogQO qo);

    IPage<SysLoginLogVO> queryLoginLogPage(LoginLogQO qo);
}
