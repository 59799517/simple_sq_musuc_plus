package com.sqmusicplus.config.webconfig.shell;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/3/8
 * Time: 16:38
 * Description:
 */
@Data
public class RequesrShell {
    @TableField(exist = false)
    @JSONField(serialize = false)
    Long pageindex = 1L;
    @JSONField(serialize = false)
    @TableField(exist = false)
    Long pagesize = 20L;
    @JSONField(serialize = false)
    @TableField(exist = false)
    String ascs;
    @JSONField(serialize = false)
    @TableField(exist = false)
    String desc;
    @JSONField(serialize = false)
    @TableField(exist = false)
    String likefield;
    @JSONField(serialize = false)
    @TableField(exist = false)
    String infield;


}
