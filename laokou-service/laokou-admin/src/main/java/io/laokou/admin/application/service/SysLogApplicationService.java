package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.qo.LoginLogQO;
import io.laokou.admin.interfaces.qo.OperateLogQO;
import io.laokou.admin.interfaces.vo.LoginLogVO;
import io.laokou.admin.interfaces.vo.OperateLogVO;
import io.laokou.common.dto.LoginLogDTO;
import io.laokou.common.dto.OperateLogDTO;

public interface SysLogApplicationService {

    Boolean insertOperateLog(OperateLogDTO dto);

    Boolean insertLoginLog(LoginLogDTO dto);

    IPage<OperateLogVO> queryOperateLogPage(OperateLogQO qo);

    IPage<LoginLogVO> queryLoginLogPage(LoginLogQO qo);
}
