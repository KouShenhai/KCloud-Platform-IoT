/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.application.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysDeptApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysDeptDO;
import org.laokou.admin.server.domain.sys.entity.SysUserDO;
import org.laokou.admin.server.domain.sys.repository.service.SysDeptService;
import org.laokou.admin.client.dto.SysDeptDTO;
import org.laokou.admin.server.domain.sys.repository.service.SysUserService;
import org.laokou.admin.server.interfaces.qo.SysDeptQo;
import org.laokou.admin.client.vo.SysDeptVO;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.swagger.utils.ValidatorUtil;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.common.core.utils.TreeUtil;
import org.laokou.redis.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author laokou
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:30
 */
@Service
@RequiredArgsConstructor
public class SysDeptApplicationServiceImpl implements SysDeptApplicationService {

    private final SysDeptService sysDeptService;

    private final SysUserService sysUserService;

    private final RedisUtil redisUtil;

    @Override
    public SysDeptVO getDeptList() {
        SysDeptQo qo = new SysDeptQo();
        String deptAllKey = RedisKeyUtil.getDeptAllKey();
        Object obj = redisUtil.get(deptAllKey);
        if (obj != null) {
            return (SysDeptVO) obj;
        }
        List<SysDeptVO> deptList = sysDeptService.getDeptList(qo);
        SysDeptVO sysDeptVO = buildDept(deptList);
        redisUtil.set(deptAllKey,sysDeptVO,RedisUtil.HOUR_ONE_EXPIRE);
        return sysDeptVO;
    }

    @Override
    public List<SysDeptVO> queryDeptList(SysDeptQo qo) {
        return sysDeptService.getDeptList(qo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertDept(SysDeptDTO dto) {
        ValidatorUtil.validateEntity(dto);
        long count = sysDeptService.count(Wrappers.lambdaQuery(SysDeptDO.class).eq(SysDeptDO::getName, dto.getName()));
        if (count > 0) {
            throw new CustomException("部门已存在，请重新填写");
        }
        SysDeptDO sysDeptDO = ConvertUtil.sourceToTarget(dto, SysDeptDO.class);
        sysDeptDO.setCreator(UserUtil.getUserId());
        sysDeptService.save(sysDeptDO);
        // 修改当前节点path
        sysDeptService.updateDeptPath1ById(sysDeptDO.getId(),sysDeptDO.getPid());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDept(SysDeptDTO dto) {
        ValidatorUtil.validateEntity(dto);
        Long id = dto.getId();
        if (id == null) {
            throw new CustomException("部门编号不为空");
        }
        long count = sysDeptService.count(Wrappers.lambdaQuery(SysDeptDO.class).eq(SysDeptDO::getName, dto.getName()).ne(SysDeptDO::getId,dto.getId()));
        if (count > 0) {
            throw new CustomException("部门已存在，请重新填写");
        }
        Integer version = sysDeptService.getVersion(id);
        SysDeptDO sysDeptDO = ConvertUtil.sourceToTarget(dto, SysDeptDO.class);
        sysDeptDO.setVersion(version);
        sysDeptDO.setEditor(UserUtil.getUserId());
        sysDeptService.updateById(sysDeptDO);
        // 修改当前节点及子节点path
        sysDeptService.updateDeptPath2ById(dto.getId(),dto.getPid());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDept(Long id) {
        long count = sysUserService.count(Wrappers.lambdaQuery(SysUserDO.class).eq(SysUserDO::getDeptId, id));
        if (count > 0) {
            throw new CustomException("不可删除，该部门下存在用户");
        }
        sysDeptService.deleteDept(id);
        String deptAllKey = RedisKeyUtil.getDeptAllKey();
        redisUtil.delete(deptAllKey);
        return true;
    }

    @Override
    public SysDeptVO getDept(Long id) {
        return sysDeptService.getDept(id);
    }

    @Override
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
