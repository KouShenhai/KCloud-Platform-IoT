package io.laokou.admin.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.laokou.admin.application.service.SysLogApplicationService;
import io.laokou.admin.domain.sys.entity.SysOperateLogDO;
import io.laokou.admin.domain.sys.repository.service.SysOperateLogService;
import io.laokou.admin.interfaces.qo.OperateLogQO;
import io.laokou.admin.interfaces.vo.OperateLogVO;
import io.laokou.common.dto.OperateLogDTO;
import io.laokou.common.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogApplicationServiceImpl implements SysLogApplicationService {

    @Autowired
    private SysOperateLogService sysOperateLogService;

    @Override
    public Boolean insertOperateLog(OperateLogDTO dto) {
        SysOperateLogDO logDO = ConvertUtil.sourceToTarget(dto, SysOperateLogDO.class);
        return sysOperateLogService.save(logDO);
    }

    @Override
    public IPage<OperateLogVO> queryOperateLogPage(OperateLogQO qo) {
        IPage<OperateLogVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysOperateLogService.operateLogPage(page,qo);
    }

}
