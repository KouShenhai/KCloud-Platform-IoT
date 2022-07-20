package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysMessageDetailDO;
import io.laokou.admin.domain.sys.repository.dao.SysMessageDetailDao;
import io.laokou.admin.domain.sys.repository.service.SysMessageDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysMessageDetailServiceImpl extends ServiceImpl<SysMessageDetailDao, SysMessageDetailDO> implements SysMessageDetailService {
}
