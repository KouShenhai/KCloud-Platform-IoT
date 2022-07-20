package io.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysOperateLogDO;
import io.laokou.admin.interfaces.qo.SysOperateLogQO;
import io.laokou.admin.interfaces.vo.SysOperateLogVO;

public interface SysOperateLogService extends IService<SysOperateLogDO> {

    IPage<SysOperateLogVO> getOperateLogList(IPage<SysOperateLogVO> page, SysOperateLogQO qo);

}
