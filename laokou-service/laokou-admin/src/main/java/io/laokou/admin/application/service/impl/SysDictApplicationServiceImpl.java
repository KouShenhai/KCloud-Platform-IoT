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
import io.laokou.datasource.annotation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysDictApplicationServiceImpl implements SysDictApplicationService {

    @Autowired
    private SysDictService sysDictService;

    @Override
    @DataSource("master")
    public IPage<SysDictVO> queryDictPage(SysDictQO qo) {
        IPage<SysDictVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysDictService.getDictList(page,qo);
    }

    @Override
    @DataSource("master")
    public SysDictVO getDictById(Long id) {
        return sysDictService.getDictById(id);
    }

    @Override
    @DataSource("master")
    public Boolean insertDict(SysDictDTO dto, HttpServletRequest request) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setCreator(SecurityUser.getUserId(request));
        return sysDictService.save(dictDO);
    }

    @Override
    @DataSource("master")
    public Boolean updateDict(SysDictDTO dto, HttpServletRequest request) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setEditor(SecurityUser.getUserId(request));
        return sysDictService.updateById(dictDO);
    }

    @Override
    @DataSource("master")
    public Boolean deleteDict(Long id) {
        sysDictService.deleteDict(id);
        return true;
    }
}
