package com.sqmusicplus.plug.aria2.aria2.callback;

import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2Response;
import com.sqmusicplus.plug.aria2.aria2.utils.JsonUtils;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 指定返回类型的回调
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/15 14:56
 */
@Setter
public abstract class ClassAria2Callback<T> extends AbstractAria2Callback {
    Class<? extends Aria2Response<T>> eClass;

    @Override
    public final void onSuccess(@NotNull String body) {
        final Aria2Response<T> response = JsonUtils.parse(body, eClass);
        onSuccess(response == null ? null : response.getResult());
    }

    /**
     * 执行成功回调
     * @param res 响应对象
     */
    abstract public void onSuccess(@Nullable T res);
}
