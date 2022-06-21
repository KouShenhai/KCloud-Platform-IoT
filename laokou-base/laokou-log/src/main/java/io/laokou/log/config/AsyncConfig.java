package io.laokou.log.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/**
 * @author  Kou Shenhai
 */
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Override
    @Bean("asyncTaskExecutor")
    public AsyncTaskExecutor getAsyncExecutor() {
        //定义线程池
        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
        //核心线程数
        threadPoolTaskExecutor.setCorePoolSize(16);
        //线程池最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(32);
        //线程队列最大线程数
        threadPoolTaskExecutor.setQueueCapacity(2000);
        //初始化
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
