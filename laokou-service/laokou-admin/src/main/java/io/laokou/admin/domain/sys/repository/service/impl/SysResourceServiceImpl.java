package io.laokou.admin.domain.sys.repository.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysResourceDO;
import io.laokou.admin.domain.sys.repository.mapper.SysResourceMapper;
import io.laokou.admin.domain.sys.repository.service.SysResourceService;
import io.laokou.admin.interfaces.qo.SysResourceQO;
import io.laokou.admin.interfaces.vo.SysResourceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 4:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResourceDO> implements SysResourceService {

    @Override
    public IPage<SysResourceVO> getResourceList(IPage<SysResourceVO> page, SysResourceQO qo) {
        return this.baseMapper.getResourceList(page, qo);
    }

    @Override
    public SysResourceVO getResourceById(Long id) {
        return this.getResourceById(id);
    }

    @Override
    public void deleteResource(Long id) {
        this.baseMapper.deleteResource(id);
    }
}
