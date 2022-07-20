package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysMessageDO;
import io.laokou.admin.domain.sys.repository.dao.SysMessageDao;
import io.laokou.admin.domain.sys.repository.service.SysMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysMessageServiceImpl extends ServiceImpl<SysMessageDao, SysMessageDO> implements SysMessageService {
}
