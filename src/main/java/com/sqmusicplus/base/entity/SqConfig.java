package com.sqmusicplus.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @Classname SqConfig
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/10/21 10:45
 * @Created by SQ
 */
@Data
@TableName(value = "sq_config")
public class SqConfig implements Serializable {
    public static final String COL_CONFIG_ID = "config_id";
    public static final String COL_CONFIG_NAME = "config_name";
    public static final String COL_CONFIG_VALUE = "config_value";
    public static final String COL_CONFIG_KEY = "config_key";
    public static final String COL_TYPE = "type";
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;
    @TableField(value = "config_name")
    private String configName;
    @TableField(value = "config_value")
    private String configValue;
    @TableField(value = "config_key")
    private String configKey;
    @TableField(value = "type")
    private String type;
}
