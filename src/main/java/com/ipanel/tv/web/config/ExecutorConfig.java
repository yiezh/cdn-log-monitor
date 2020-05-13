package com.ipanel.tv.web.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * remark
 *
 * @author luzh
 * createTime 2017年6月23日 下午5:10:45
 */
@EnableAsync
@Configuration
public class ExecutorConfig extends AsyncConfigurerSupport {

    private final BeanFactory beanFactory;

    public ExecutorConfig(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int core = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(core * 2 + 1);
        //如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        //executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("pool-thread-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setKeepAliveSeconds(60);
        executor.initialize();
        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));
        return new LazyTraceExecutor(beanFactory, executor);
    }


    @Override
    public Executor getAsyncExecutor() {
        return executor();
    }
}
