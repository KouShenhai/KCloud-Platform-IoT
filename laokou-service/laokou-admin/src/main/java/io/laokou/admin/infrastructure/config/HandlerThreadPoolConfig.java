package io.laokou.admin.infrastructure.config;

import com.dtp.common.em.QueueTypeEnum;
import com.dtp.common.em.RejectedTypeEnum;
import com.dtp.core.support.ThreadPoolBuilder;
import com.dtp.core.thread.DtpExecutor;

import java.util.concurrent.TimeUnit;

public class HandlerThreadPoolConfig {

    /**
     * 处理某个渠道的某种类型消息的线程池
     * 配置：不丢弃消息，核心线程数不随着keepAliveTime而减少(不会被回收)
     * 动态线程池且被spring管理
     */
    public static DtpExecutor getExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("admin-service")
                .corePoolSize(8)
                .corePoolSize(8)
                .keepAliveTime(60)
                .timeUnit(TimeUnit.SECONDS)
                .rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
                .allowCoreThreadTimeOut(false)
                .workQueue(QueueTypeEnum.VARIABLE_LINKED_BLOCKING_QUEUE.getName(),256,false)
                .buildDynamic();
    }

}
