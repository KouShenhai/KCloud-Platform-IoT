package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.laokou.admin.application.service.SysDeptApplicationService;
import io.laokou.admin.domain.sys.entity.SysDeptDO;
import io.laokou.admin.domain.sys.repository.service.SysDeptService;
import io.laokou.admin.interfaces.dto.SysDeptDTO;
import io.laokou.admin.interfaces.qo.SysDeptQO;
import io.laokou.common.constant.Constant;
import io.laokou.common.exception.CustomException;
import io.laokou.common.user.SecurityUser;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.common.utils.TreeUtil;
import io.laokou.common.vo.SysDeptVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:30
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysDeptApplicationServiceImpl implements SysDeptApplicationService {

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public SysDeptVO getDeptList() {
        SysDeptQO qo = new SysDeptQO();
        qo.setStatus(Constant.NO);
        List<SysDeptVO> deptList = sysDeptService.getDeptList(qo);
        return buildDept(deptList);
    }

    @Override
    public List<SysDeptVO> queryDeptList(SysDeptQO qo) {
        return sysDeptService.getDeptList(qo);
    }

    @Override
    public Boolean insertDept(SysDeptDTO dto, HttpServletRequest request) {
        SysDeptDO sysDeptDO = ConvertUtil.sourceToTarget(dto, SysDeptDO.class);
        int count = sysDeptService.count(Wrappers.lambdaQuery(SysDeptDO.class).eq(SysDeptDO::getName, dto.getName()).eq(SysDeptDO::getDelFlag, Constant.NO));
        if (count > 0) {
            throw new CustomException("部门已存在，请重新填写");
        }
        sysDeptDO.setCreator(SecurityUser.getUserId(request));
        return sysDeptService.save(sysDeptDO);
    }

    @Override
    public Boolean updateDept(SysDeptDTO dto,HttpServletRequest request) {
        SysDeptDO sysDeptDO = ConvertUtil.sourceToTarget(dto, SysDeptDO.class);
        int count = sysDeptService.count(Wrappers.lambdaQuery(SysDeptDO.class).eq(SysDeptDO::getName, dto.getName()).eq(SysDeptDO::getDelFlag, Constant.NO).ne(SysDeptDO::getId,dto.getId()));
        if (count > 0) {
            throw new CustomException("部门已存在，请重新填写");
        }
        sysDeptDO.setEditor(SecurityUser.getUserId(request));
        return sysDeptService.updateById(sysDeptDO);
    }

    @Override
    public Boolean deleteDept(Long id) {
        return null;
    }

    @Override
    public SysDeptVO getDept(Long id) {
        return null;
    }

    /**
     * 组装树部门
     * @param deptList
     * @return
     */
    private SysDeptVO buildDept(List<SysDeptVO> deptList) {
        TreeUtil.TreeNo<TreeUtil.TreeNo> rootNode = TreeUtil.rootRootNode();
        SysDeptVO rootDeptNode = ConvertUtil.sourceToTarget(rootNode, SysDeptVO.class);
        return TreeUtil.buildTreeNode(deptList,rootDeptNode);
    }

}
