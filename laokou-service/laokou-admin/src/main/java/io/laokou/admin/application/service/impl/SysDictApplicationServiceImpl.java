package io.laokou.admin.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.laokou.admin.application.service.SysDictApplicationService;
import io.laokou.admin.domain.sys.entity.SysDictDO;
import io.laokou.admin.domain.sys.repository.service.SysDictService;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.SysDictDTO;
import io.laokou.admin.interfaces.qo.SysDictQO;
import io.laokou.admin.interfaces.vo.SysDictVO;
import io.laokou.common.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictApplicationServiceImpl implements SysDictApplicationService {

    @Autowired
    private SysDictService sysDictService;

    @Override
    public IPage<SysDictVO> queryDictPage(SysDictQO qo) {
        IPage<SysDictVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysDictService.getDictList(page,qo);
    }

    @Override
    public SysDictVO getDictById(Long id) {
        return sysDictService.getDictById(id);
    }

    @Override
    public Boolean insertDict(SysDictDTO dto, HttpServletRequest request) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setCreator(SecurityUser.getUserId(request));
        return sysDictService.save(dictDO);
    }

    @Override
    public Boolean updateDict(SysDictDTO dto, HttpServletRequest request) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setEditor(SecurityUser.getUserId(request));
        return sysDictService.updateById(dictDO);
    }

    @Override
    public Boolean deleteDict(Long id) {
        sysDictService.deleteDict(id);
        return true;
    }
}
