package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysResourceApplicationService;
import io.laokou.admin.interfaces.dto.SysResourceDTO;
import io.laokou.admin.interfaces.qo.SysResourceQO;
import io.laokou.admin.interfaces.vo.SysResourceVO;
import io.laokou.datasource.annotation.DataFilter;
import io.laokou.datasource.annotation.DataSource;
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

    @Override
    @DataSource("master")
    @DataFilter(tableAlias = "boot_sys_resource")
    public IPage<SysResourceVO> queryResourcePage(SysResourceQO qo) {

        return null;
    }

    @Override
    @DataSource("master")
    public SysResourceVO getResourceById(Long id) {
        return null;
    }

    @Override
    @DataSource("master")
    public Boolean insertResource(SysResourceDTO dto, HttpServletRequest request) {
        return null;
    }

    @Override
    @DataSource("master")
    public Boolean updateResource(SysResourceDTO dto, HttpServletRequest request) {
        return null;
    }

    @Override
    @DataSource("master")
    public Boolean deleteResource(Long id) {
        return null;
    }

}
