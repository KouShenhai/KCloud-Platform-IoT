package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysDictDO;
import io.laokou.admin.domain.sys.repository.dao.SysDictDao;
import io.laokou.admin.domain.sys.repository.service.SysDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/6/23 0023 上午 11:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictServiceImpl extends ServiceImpl<SysDictDao,SysDictDO> implements SysDictService {
}
