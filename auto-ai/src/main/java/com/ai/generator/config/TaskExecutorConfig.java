package com.ai.generator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Administrator
 */
@Configuration
@EnableAsync
public class TaskExecutorConfig {
    @Autowired
    TaskConfig taskConfig;

    @Bean("asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        ThreadPoolTaskExecutor taskExecutor = new ContextAwarePoolExecutor();
                //线程池核心线程数
        taskExecutor.setCorePoolSize(taskConfig.getCorePoolSize());
        //线程池最大线程数
        taskExecutor.setMaxPoolSize(taskConfig.getMaxPoolSize());
        //缓冲队列
        taskExecutor.setQueueCapacity(taskConfig.getQueueCapacity());
        //线程名称前缀
        taskExecutor.setThreadNamePrefix(taskConfig.getNamePrefix());
        //线程空闲后的最大存活时间
        taskExecutor.setKeepAliveSeconds(taskConfig.getKeepAliveSeconds());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(60);
        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        taskExecutor.initialize();
        return taskExecutor;
    }

}
