package com.sqmusicplus.plug.aria2.aria2.callback;


import com.sqmusicplus.plug.aria2.aria2.exception.Aria2Exception;
import com.sqmusicplus.plug.aria2.aria2.exception.Aria2RequestException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 抽象Aria2回调
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/15 14:23
 */
public abstract class AbstractAria2Callback implements Callback {
    /**
     * 处理响应
     * @param call     call
     * @param response 响应
     * @return body
     * @throws Aria2RequestException 异常
     */
    public static ResponseBody body(@NotNull Call call, @NotNull Response response) throws Aria2RequestException {
        final int code = response.code();
        final int co = code / 100;
        switch (co) {
            case 3:
            case 2:
                return response.body();
            case 4:
                throw new Aria2RequestException(code, response.message(), call);
            case 5:
                throw new Aria2RequestException(code, "服务器异常", call);
            default:
                throw new Aria2RequestException(code, "非预期的code", call);
        }
    }

    @Override
    public final void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        try {
            try (ResponseBody body = body(call, response)) {
                if (body != null) {
                    onSuccess(body.string());
                }
            }
        } catch (Aria2Exception e) {
            handleException(e);
        }

    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        e.printStackTrace();
    }

    /**
     * 执行成功回调
     * @param body body字符串
     */
    public abstract void onSuccess(@NotNull String body);

    /**
     * 处理异常
     * @param e 异常
     */
    public void handleException(Aria2Exception e) {
        e.printStackTrace();
    }

}
