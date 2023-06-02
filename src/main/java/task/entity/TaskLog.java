package task.entity;

import java.util.Date;

/**
 * @Classname TaskLog
 * @Description 任务日志
 * @Version 1.0.0
 * @Date 2023/5/31 11:03
 * @Created by shang
 */

public class TaskLog {
    String log;
    Date logTime;

    public TaskLog() {
    }

    public TaskLog(String log) {
        this.log = log;
        this.logTime= new Date();
    }

    public TaskLog(String log, Date logTime) {
        this.log = log;
        this.logTime = logTime;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }


}
