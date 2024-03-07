package com.sqmusicplus.plug.aria2.aria2.dto.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;

import java.util.List;

/**
 * 请求参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/10/21 11:19
 */
@Getter
public class Aria2RequestBody {
    @JsonSerialize(using = ToStringSerializer.class)
    final int id;
    @JsonSerialize(using = ToStringSerializer.class)
    final double jsonrpc = 2.0;
    final String method;
    final List<Object> params;

    public Aria2RequestBody(int id, String method, List<Object> params) {
        this.id = id;
        this.method = method;
        this.params = params;
    }


}
