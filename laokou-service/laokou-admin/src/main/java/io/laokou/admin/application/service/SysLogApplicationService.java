package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.common.dto.OperateLogDTO;

public interface SysLogApplicationService {

    Boolean insertOperateLog(OperateLogDTO dto);

}
