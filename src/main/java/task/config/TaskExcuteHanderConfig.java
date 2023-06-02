package task.config;

import cn.hutool.core.thread.ExecutorBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Classname TaskExcuteHanderConfig
 * @Description 任务执行器设置
 * @Version 1.0.0
 * @Date 2023/5/31 10:45
 * @Created by shang
 */

public class TaskExcuteHanderConfig {


    private ExecutorService executor;

    /**
     * 初始化任务处理器配置
     * @param CorePoolSize 初始化线程数
     * @param MaxPoolSize 最大线程数
     * @param WorkQueueSize 有界等待队列，最大等待数 超过数量会抛出异常
     */
    public TaskExcuteHanderConfig(Integer CorePoolSize , Integer MaxPoolSize, Integer WorkQueueSize) {
        executor = ExecutorBuilder.create()
                .setCorePoolSize(CorePoolSize)
                .setMaxPoolSize(MaxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(WorkQueueSize))
                .build();
    }
    public TaskExcuteHanderConfig() {
        executor = ExecutorBuilder.create()
                .setCorePoolSize(10)
                .setMaxPoolSize(50)
                .setWorkQueue(new LinkedBlockingQueue<>(0))
                .build();
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}
