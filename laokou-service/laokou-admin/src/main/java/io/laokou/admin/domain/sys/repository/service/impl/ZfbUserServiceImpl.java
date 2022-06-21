package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.ZfbUserDO;
import io.laokou.admin.domain.sys.repository.dao.ZfbUserDao;
import io.laokou.admin.domain.sys.repository.service.ZfbUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ZfbUserServiceImpl extends ServiceImpl<ZfbUserDao, ZfbUserDO> implements ZfbUserService {
}
