/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.laokou.admin.application.service.SysDeptApplicationService;
import io.laokou.admin.domain.sys.entity.SysDeptDO;
import io.laokou.admin.domain.sys.repository.service.SysDeptService;
import io.laokou.admin.interfaces.dto.SysDeptDTO;
import io.laokou.admin.interfaces.qo.SysDeptQO;
import org.laokou.common.constant.Constant;
import org.laokou.common.exception.CustomException;
import org.laokou.common.user.SecurityUser;
import org.laokou.common.utils.ConvertUtil;
import org.laokou.common.utils.TreeUtil;
import org.laokou.common.vo.SysDeptVO;
import org.laokou.datasource.annotation.DataSource;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:30
 */
@Service
@GlobalTransactional(rollbackFor = Exception.class)
public class SysDeptApplicationServiceImpl implements SysDeptApplicationService {

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    @DataSource("master")
    public SysDeptVO getDeptList() {
        SysDeptQO qo = new SysDeptQO();
        qo.setStatus(Constant.NO);
        List<SysDeptVO> deptList = sysDeptService.getDeptList(qo);
        return buildDept(deptList);
    }

    @Override
    @DataSource("master")
    public List<SysDeptVO> queryDeptList(SysDeptQO qo) {
        return sysDeptService.getDeptList(qo);
    }

    @Override
    @DataSource("master")
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
    @DataSource("master")
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
    @DataSource("master")
    public Boolean deleteDept(Long id) {
        sysDeptService.deleteDept(id);
        return true;
    }

    @Override
    @DataSource("master")
    public SysDeptVO getDept(Long id) {
        return sysDeptService.getDept(id);
    }

    @Override
    @DataSource("master")
    public List<Long> getDeptIdsByRoleId(Long roleId) {
        return sysDeptService.getDeptIdsByRoleId(roleId);
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
