package io.laokou.auth.domain.sys.repository.service;

import io.laokou.common.vo.SysDeptVO;
import java.util.List;

public interface SysDeptService {

    List<SysDeptVO> getDeptListByUserId(Long userId);

    List<SysDeptVO> getDeptList();

}
