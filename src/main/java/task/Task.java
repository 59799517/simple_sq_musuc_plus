package task;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import task.entity.TaskConfig;
import task.entity.TaskLog;
import task.entity.enums.TaskStatus;


import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Classname Task
 * @Description 任务
 * @Version 1.0.0
 * @Date 2023/5/31 10:05
 * @Created by shang
 */

public class Task<T> {

    /**
     * 任务id
     */
    private String taskID;
    /**
     * 任务名称
     */
    private String taskNmae;

    /**
     * 任务配置
     */
    private TaskConfig taskConfig;
    /**
     * 任务日志
     */
    private CopyOnWriteArrayList<TaskLog> taskLogs;

    /**
     * 通讯信息（再用户执行任务时所用的自定义信息）
     */
    private String communicationInformation;

    /**
     * 返回结果
     */
    private T result;
    /**
     * 任务状态
     */
    private TaskStatus status;

    @JsonIgnore
    private Callable<T> action;
    /**
     * 执行完成后通知下一个任务
     */
    private Task<T> nextTask;
    /**
     * 上一步执行结果
     */
    private T previous;





    public Task(String taskNmae) {
        this.taskNmae = taskNmae;
        this.taskID = IdUtil.randomUUID();
        this.taskLogs= new CopyOnWriteArrayList<>();
        this.status = TaskStatus.NOT_START;
    }

    public Task(String taskNmae, TaskConfig taskConfig) {
        this.taskNmae = taskNmae;
        this.taskID = IdUtil.randomUUID();
        this.taskConfig = taskConfig;
        this.status = TaskStatus.NOT_START;
    }

    protected void excute() {
        if (this.taskConfig!=null&&this.taskConfig.getSecond()!=null) {
            try {
                Thread.sleep(this.taskConfig.getSecond()*1000);
            } catch (InterruptedException e) {
                this.taskLogs.add(new TaskLog("执行异常："+this.taskNmae +" " +e.getMessage(), new Date()));
            }
        }
        this.status = TaskStatus.RUNNING;
        try {
            this.taskLogs.add(new TaskLog("开始任务："+this.taskNmae, new Date()));
            Callable<T> taskCallable = Objects.requireNonNull(action);
            T call = taskCallable.call();
            if (nextTask != null) {
                nextTask.setPrevious(call);
                nextTask.excute();
            }
            this.setResult(call);
            taskLogs.add(new TaskLog("执行结束："+this.taskNmae, new Date()));
        } catch (Exception e) {
            this.status = TaskStatus.ERROR;
            this.taskLogs.add(new TaskLog("执行异常："+this.taskNmae+e.getMessage(), new Date()));
        }
        this.status=TaskStatus.SUCCESS;
    }


    public Boolean isOver(){
        if (this.getStatus().equals(TaskStatus.SUCCESS)||this.getStatus().equals(TaskStatus.ERROR)) {
            return true;
        }
        return false;
    }
    public Boolean isRunning(){
        if (this.getStatus().equals(TaskStatus.RUNNING)) {
            return true;
        }
        return false;
    }
    public TaskConfig getTaskConfig() {
        return taskConfig;
    }

    public void setTaskConfig(TaskConfig taskConfig) {
        this.taskConfig = taskConfig;
    }



    public String getCommunicationInformation() {
        return communicationInformation;
    }

    protected void setCommunicationInformation(String communicationInformation) {
        this.communicationInformation = communicationInformation;
    }

    public List<TaskLog> getTaskLogs() {
        return taskLogs;
    }

    public void setTaskLogs(CopyOnWriteArrayList<TaskLog> taskLogs) {
        this.taskLogs = taskLogs;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public T getResult() {
        return result;
    }

    public String getTaskNmae() {
        return taskNmae;
    }

    public void setTaskNmae(String taskNmae) {
        this.taskNmae = taskNmae;
    }

    public String getTaskID() {
        return taskID;
    }

    protected void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    protected void setResult(T result) {
        this.result = result;
    }

    public void setAction(Callable<T> action) {
        this.action = action;
    }

    public T getPrevious() {
        return previous;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    public Task<T> getNextTask() {
        return nextTask;
    }

    public void setNextTask(Task<T> nextTask) {
        this.nextTask = nextTask;
    }
}
