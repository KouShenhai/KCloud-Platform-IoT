package io.laokou.admin.infrastructure.component.utils;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolUtil {

    @Autowired
    private ThreadPoolExecutorShutdownDefinition shutdownDefinition;

    /**
     * 1.当前线程池 加入到 动态线程池内
     * 2.注册线程池被spring管理，优雅关闭
     */
    public void register(DtpExecutor dtpExecutor) {
        DtpRegistry.registerDtp(dtpExecutor,"laokou");
        shutdownDefinition.registryExecutor(dtpExecutor);
    }

}
