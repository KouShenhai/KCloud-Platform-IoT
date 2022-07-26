package io.laokou.admin.application.service.impl;
import io.laokou.admin.application.service.SysDeptApplicationService;
import io.laokou.admin.domain.sys.repository.service.SysDeptService;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.common.utils.TreeUtil;
import io.laokou.common.vo.SysDeptVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
        List<SysDeptVO> deptList = sysDeptService.getDeptList();
        return buildDept(deptList);
    }

    @Override
    public List<SysDeptVO> queryDeptList() {
        return sysDeptService.getDeptList();
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
