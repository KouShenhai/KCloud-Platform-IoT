package io.laokou.auth.domain.sys.repository.service.impl;

import io.laokou.auth.domain.sys.repository.mapper.SysDeptMapper;
import io.laokou.auth.domain.sys.repository.service.SysDeptService;
import io.laokou.common.vo.SysDeptVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public List<SysDeptVO> getDeptListByUserId(Long userId) {
        return sysDeptMapper.getDeptListByUserId(userId);
    }

    @Override
    public List<SysDeptVO> getDeptList() {
        return sysDeptMapper.getDeptList();
    }
}
