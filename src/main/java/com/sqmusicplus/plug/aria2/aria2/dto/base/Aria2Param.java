package com.sqmusicplus.plug.aria2.aria2.dto.base;

import com.sqmusicplus.plug.aria2.aria2.enums.Aria2Method;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 请求参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/15 14:45
 */
@Getter
public class Aria2Param {
    @NotNull
    final Aria2Method methodName;
    @NotNull
    final List<Object> params;

    public Aria2Param(@NotNull Aria2Method methodName, @NotNull List<Object> params) {
        this.methodName = methodName;
        this.params = params;
    }

    public Aria2Param(@NotNull Aria2Method methodName, Object... params) {
        this.methodName = methodName;
        this.params = new ArrayList<>(Arrays.asList(params));;
    }

    public Aria2Param(@NotNull Aria2Method methodName, String gid) {
        this.methodName = methodName;
        this.params = Collections.singletonList(gid);
    }

    /**
     * 分页查询参数的构造方法
     */
    public Aria2Param(@NotNull Aria2Method methodName, int page, int size, String... keys) {
        this.methodName = methodName;
        this.params = Arrays.asList(Math.max(0, (page - 1) * size), size, Arrays.asList(keys));
    }

    public static List<Aria2Param> listOf(@NotNull Aria2Method methodName, Collection<String> gid){
        return gid.stream().map(g -> new Aria2Param(methodName, g)).collect(Collectors.toList());
    }

}
