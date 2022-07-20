package io.laokou.admin.infrastructure.component.run;

import com.dtp.core.thread.DtpExecutor;
import io.laokou.admin.infrastructure.component.utils.ThreadPoolUtil;
import io.laokou.admin.infrastructure.config.HandlerThreadPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;

@Component
public class TaskPendingHolder {

    @Autowired
    private ThreadPoolUtil threadPoolUtil;

    private ExecutorService taskPendingHolder;

    @PostConstruct
    public void init() {
        DtpExecutor executor = HandlerThreadPoolConfig.getExecutor();
        threadPoolUtil.register(executor);
        taskPendingHolder = executor;
    }

    public ExecutorService route() {
        return taskPendingHolder;
    }

}
