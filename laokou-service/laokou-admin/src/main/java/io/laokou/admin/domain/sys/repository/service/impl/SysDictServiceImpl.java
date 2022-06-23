package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysDictDO;
import io.laokou.admin.domain.sys.repository.dao.SysDictDao;
import io.laokou.admin.domain.sys.repository.service.SysDictService;
import io.laokou.admin.interfaces.qo.DictQO;
import io.laokou.admin.interfaces.vo.DictVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/6/23 0023 上午 11:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictServiceImpl extends ServiceImpl<SysDictDao,SysDictDO> implements SysDictService {

    @Override
    public List<DictVO> getDictList(DictQO qo) {
        return this.baseMapper.getDictList(qo);
    }
}
