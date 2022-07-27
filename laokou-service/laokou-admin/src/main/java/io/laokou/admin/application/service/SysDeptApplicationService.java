package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.SysDeptDTO;
import io.laokou.admin.interfaces.qo.SysDeptQO;
import io.laokou.common.vo.SysDeptVO;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:29
 */
public interface SysDeptApplicationService {

    SysDeptVO getDeptList();

    List<SysDeptVO> queryDeptList(SysDeptQO qo);

    Boolean insertDept(SysDeptDTO dto, HttpServletRequest request);

    Boolean updateDept(SysDeptDTO dto, HttpServletRequest request);

    Boolean deleteDept(Long id);

    SysDeptVO getDept(Long id);

}
