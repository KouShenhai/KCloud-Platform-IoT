package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.laokou.admin.application.service.SysResourceApplicationService;
import io.laokou.admin.domain.sys.entity.SysResourceDO;
import io.laokou.admin.domain.sys.repository.service.SysResourceService;
import io.laokou.admin.interfaces.dto.SysResourceDTO;
import io.laokou.admin.interfaces.qo.SysResourceQO;
import io.laokou.admin.interfaces.vo.SysResourceVO;
import io.laokou.common.user.SecurityUser;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.datasource.annotation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:43
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysResourceApplicationServiceImpl implements SysResourceApplicationService {

    @Autowired
    private SysResourceService sysResourceService;

    @Override
    @DataSource("master")
    public IPage<SysResourceVO> queryResourcePage(SysResourceQO qo) {
        IPage<SysResourceVO> page = new Page(qo.getPageNum(),qo.getPageSize());
        return sysResourceService.getResourceList(page,qo);
    }

    @Override
    @DataSource("master")
    public SysResourceVO getResourceById(Long id) {
        return sysResourceService.getResourceById(id);
    }

    @Override
    @DataSource("master")
    public Boolean insertResource(SysResourceDTO dto, HttpServletRequest request) {
        SysResourceDO sysResourceDO = ConvertUtil.sourceToTarget(dto, SysResourceDO.class);
        sysResourceDO.setCreator(SecurityUser.getUserId(request));
        sysResourceDO.setAuthor(SecurityUser.getUsername(request));
        return sysResourceService.save(sysResourceDO);
    }

    @Override
    @DataSource("master")
    public Boolean updateResource(SysResourceDTO dto, HttpServletRequest request) {
        SysResourceDO sysResourceDO = ConvertUtil.sourceToTarget(dto, SysResourceDO.class);
        sysResourceDO.setEditor(SecurityUser.getUserId(request));
        return sysResourceService.updateById(sysResourceDO);
    }

    @Override
    @DataSource("master")
    public Boolean deleteResource(Long id) {
        sysResourceService.deleteResource(id);
        return true;
    }

}
