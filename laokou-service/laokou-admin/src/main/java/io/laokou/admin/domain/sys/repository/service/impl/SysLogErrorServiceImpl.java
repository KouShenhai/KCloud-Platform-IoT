package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysLogErrorDO;
import io.laokou.admin.domain.sys.repository.dao.SysLogErrorDao;
import io.laokou.admin.domain.sys.repository.service.SysLogErrorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogErrorServiceImpl extends ServiceImpl<SysLogErrorDao, SysLogErrorDO> implements SysLogErrorService {
}
