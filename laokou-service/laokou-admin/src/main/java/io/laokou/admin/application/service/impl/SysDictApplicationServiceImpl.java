package io.laokou.admin.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.laokou.admin.application.service.SysDictApplicationService;
import io.laokou.admin.domain.sys.entity.SysDictDO;
import io.laokou.admin.domain.sys.repository.service.SysDictService;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.DictDTO;
import io.laokou.admin.interfaces.qo.DictQO;
import io.laokou.admin.interfaces.vo.DictVO;
import io.laokou.common.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SysDictApplicationServiceImpl implements SysDictApplicationService {

    @Autowired
    private SysDictService sysDictService;

    @Override
    public IPage<DictVO> queryDictPage(DictQO qo) {
        IPage<DictVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysDictService.getDictList(page,qo);
    }

    @Override
    public DictVO getDictById(Long id) {
        return sysDictService.getDictById(id);
    }

    @Override
    public Boolean insertDict(DictDTO dto, HttpServletRequest request) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setCreator(SecurityUser.getUserId(request));
        return sysDictService.save(dictDO);
    }

    @Override
    public Boolean updateDict(DictDTO dto, HttpServletRequest request) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setEditor(SecurityUser.getUserId(request));
        return sysDictService.updateById(dictDO);
    }

    @Override
    public Boolean deleteDict(Long id) {
        sysDictService.deleteDict(id);
        return Boolean.TRUE;
    }
}
