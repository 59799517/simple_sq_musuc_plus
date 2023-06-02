package task;


import com.sqmusicplus.utils.StringUtils;
import task.config.TaskExcuteHanderConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @Classname TaskExcuteHander
 * @Description 任务处理器
 * @Version 1.0.0
 * @Date 2023/5/31 10:29
 * @Created by shang
 */

public class TaskExcuteHander <T>{

    /**
     * 任务列表
     */
    private List<Task<T>> tasks;
    /**
     * 任务配置
     */
    TaskExcuteHanderConfig taskExcuteHanderConfig;



    public TaskExcuteHander(TaskExcuteHanderConfig taskExcuteHanderConfig) {
        this.tasks = new ArrayList<>();
        this.taskExcuteHanderConfig = taskExcuteHanderConfig;
    }

    public TaskExcuteHander() {
        this.tasks = new ArrayList<>();
        this.taskExcuteHanderConfig  =new TaskExcuteHanderConfig();
    }

    /**
     * 启动任务
     */
    public void start(List<Task<T>> tasks){
        ExecutorService executor = taskExcuteHanderConfig.getExecutor();
        this.tasks.addAll(tasks);
        if (tasks.size() > 0) {
            for (Task<T> task : tasks) {
                executor.execute(()->{
                    try {
                        task.excute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    /**
     * 增加任务(增加后立即执行)
     * @param task
     * @return
     */
    public Boolean start(Task<T> task){
        Task<T> tTask = Objects.requireNonNull(task);
        boolean add = tasks.add(tTask);
        if (add){
            ExecutorService executor = taskExcuteHanderConfig.getExecutor();
            executor.execute(()->{
                try {
                    task.excute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return add;
    }

    /**
     * 删除任务
     * @param task
     * @return null 是删除失败 成功则返回当前任务
     */
    public Task<T> removeTask(Task<T> task){
        Task<T> tTask = Objects.requireNonNull(task);
        boolean remove = tasks.remove(tTask);
        if (remove){
            return task;
        }
        return null;
    }
    public List<Task<T>> removeTask(List<Task<T>> tasks){
        if (StringUtils.isNotEmpty(tasks)) {
            if (tasks.removeAll(tasks)) {
                return tasks;
            }
            return null;
        }

        return null;
    }

    /**
     * 获取任务个数
     * @return
     */
    public Integer getTaskSize(){
        return tasks.size();
    }

    /**
     * 未完成的长度
     * @return
     */
    public Integer getTaskOverSize(){
        return tasks.stream().filter(task -> task.isOver()).collect(Collectors.toList()).size();
    }

    /**
     * 未完成的长度
     * @return
     */
    public Integer getRunningTasKSize(){
        return tasks.stream().filter(task -> task.isRunning()).collect(Collectors.toList()).size();
    }

    /**
     * 获取未完成的任务
     * @return
     */
    public List<Task<T>> getOverTask(){
        return tasks.stream().filter(task -> task.isOver()).collect(Collectors.toList());
    }

    /**
     * 获取当前全部任务
     * @return
     */
    public List<Task<T>> getTasks() {
        return tasks;
    }

}
