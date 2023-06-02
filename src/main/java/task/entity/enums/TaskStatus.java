package task.entity.enums;

/**
 * @Classname TaskStatus
 * @Description 任务状态
 * @Version 1.0.0
 * @Date 2023/5/31 10:23
 * @Created by shang
 */
public enum TaskStatus {
    /**
     * 成功
     */
    SUCCESS("SUCCESS"),
    /**
     * 失败
     */
    ERROR("ERROR"),
    /**
     * 未开始
     */
    NOT_START("NOT_START"),
    /**
     * 执行中
     */
    RUNNING("RUNNING");


    String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    TaskStatus(String value) {
        this.value = value;
    }
}
