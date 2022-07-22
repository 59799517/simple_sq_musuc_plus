package com.sqmusicplus.config.webconfig.entity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.EqualsAndHashCode;


/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2019/7/2
 * Time: 11:32
 * Description:页面对象
 */
@EqualsAndHashCode
//@Accessors(chain = true)
//@AllArgsConstructor
//@ApiModel(value = "全局返回值", description = "全局的返回值对象")
public class SymbolPlus {

    private QueryWrapper queryWrapper;
    // http 状态码
//    @ApiModelProperty(value = "第几页")
    private Long pageindex;
    //    @ApiModelProperty(value = "每页长度")
    private Long pagesize;
    //    @ApiModelProperty(value = "正序字段")
    private String ascs;
    //    @ApiModelProperty(value = "倒叙字段")
    private String desc;
    //    @ApiModelProperty(value = "图表查询字段")
    private String field;

    public QueryWrapper getQueryWrapper() {
        return queryWrapper;
    }

    public void setQueryWrapper(QueryWrapper queryWrapper) {
        this.queryWrapper = queryWrapper;
    }

    public Long getPageindex() {
        return pageindex;
    }

    public void setPageindex(Long pageindex) {
        this.pageindex = pageindex;
    }

    public Long getPagesize() {
        return pagesize;
    }

    public void setPagesize(Long pagesize) {
        this.pagesize = pagesize;
    }

    public String getAscs() {
        return ascs;
    }

    public void setAscs(String ascs) {
        this.ascs = ascs;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
