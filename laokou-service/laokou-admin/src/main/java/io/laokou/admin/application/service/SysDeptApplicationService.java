package io.laokou.admin.application.service;

import io.laokou.common.vo.SysDeptVO;
import java.util.*;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:29
 */
public interface SysDeptApplicationService {

    SysDeptVO getDeptList();

    List<SysDeptVO> queryDeptList();

}
