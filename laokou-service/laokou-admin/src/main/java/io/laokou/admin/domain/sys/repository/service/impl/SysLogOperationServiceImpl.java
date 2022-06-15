package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysLogOperationDO;
import io.laokou.admin.domain.sys.repository.dao.SysLogOperationDao;
import io.laokou.admin.domain.sys.repository.service.SysLogOperationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogOperationServiceImpl extends ServiceImpl<SysLogOperationDao, SysLogOperationDO> implements SysLogOperationService {
}
