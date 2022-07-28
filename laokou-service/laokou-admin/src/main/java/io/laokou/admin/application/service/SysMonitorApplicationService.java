package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.vo.CacheVO;
import io.laokou.admin.interfaces.vo.ServerVO;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/27 0027 下午 3:17
 */
public interface SysMonitorApplicationService {

    CacheVO getCacheInfo();

    ServerVO getServerInfo() throws Exception;

}
