package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.qo.OperateLogQO;
import io.laokou.admin.interfaces.vo.OperateLogVO;
import io.laokou.common.dto.OperateLogDTO;

public interface SysLogApplicationService {

    Boolean insertOperateLog(OperateLogDTO dto);

    IPage<OperateLogVO> queryOperateLogPage(OperateLogQO qo);
}
