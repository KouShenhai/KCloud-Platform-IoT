package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysOauthDO;
import io.laokou.admin.domain.sys.repository.mapper.SysOauthMapper;
import io.laokou.admin.domain.sys.repository.service.SysOauthService;
import io.laokou.admin.interfaces.qo.SysOauthQO;
import io.laokou.admin.interfaces.vo.SysOauthVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/11 0011 上午 9:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysOauthServiceImpl extends ServiceImpl<SysOauthMapper, SysOauthDO> implements SysOauthService {
    @Override
    public void deleteOauth(Long id) {
        this.baseMapper.deleteOauth(id);
    }

    @Override
    public SysOauthVO getOauthById(Long id) {
        return this.baseMapper.getOauthById(id);
    }

    @Override
    public IPage<SysOauthVO> getOauthList(IPage<SysOauthVO> page, SysOauthQO qo) {
        return this.baseMapper.getOauthList(page,qo);
    }
}
