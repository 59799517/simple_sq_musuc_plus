package com.sqmusicplus.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName task
 */
@Data
@TableName("task")
public class Task implements Serializable {
    /**
     * 
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    private String createTime;

    /**
     * 
     */
    private String musicInfo;

    private static final long serialVersionUID = 1L;

}