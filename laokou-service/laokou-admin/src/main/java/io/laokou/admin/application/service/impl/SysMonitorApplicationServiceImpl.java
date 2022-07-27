package io.laokou.admin.application.service.impl;
import io.laokou.admin.application.service.SysMonitorApplicationService;
import io.laokou.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/27 0027 下午 3:18
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysMonitorApplicationServiceImpl implements SysMonitorApplicationService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void getRedisInfo() {

    }
}
