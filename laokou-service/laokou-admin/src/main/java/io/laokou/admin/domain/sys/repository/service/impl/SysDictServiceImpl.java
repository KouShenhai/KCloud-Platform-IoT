package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysDictDO;
import io.laokou.admin.domain.sys.repository.dao.SysDictDao;
import io.laokou.admin.domain.sys.repository.service.SysDictService;
import io.laokou.admin.interfaces.qo.SysDictQO;
import io.laokou.admin.interfaces.vo.SysDictVO;
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
    public List<SysDictVO> getDictList(SysDictQO qo) {
        return this.baseMapper.getDictList(qo);
    }

    @Override
    public IPage<SysDictVO> getDictList(IPage<SysDictVO> page, SysDictQO qo) {
        return this.baseMapper.getDictList(page,qo);
    }

    @Override
    public SysDictVO getDictById(Long id) {
        return this.baseMapper.getDictById(id);
    }

    @Override
    public void deleteDict(Long id) {
        this.baseMapper.deleteDict(id);
    }

}
