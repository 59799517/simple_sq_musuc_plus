package com.sqmusicplus.plug.aria2.aria2.main;



import com.sqmusicplus.plug.aria2.aria2.call.Aria2MethodCall;
import com.sqmusicplus.plug.aria2.aria2.exception.Aria2RequestException;
import com.sqmusicplus.plug.aria2.aria2.response.result.Aria2Version;
import com.sqmusicplus.plug.aria2.aria2.utils.JsonUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 演示
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/16 11:55
 */
public class Demo {
    public static void main(String[] args) throws Aria2RequestException, IOException {
//        isPing();
        // 1、 创建客户端,也可以使用另一个构造函数, 指定OkHttp客户端, Aria2地址, 密码
        final Aria2Client client = new Aria2Client("http://100.122.250.55:7044/jsonrpc","shangqi123");
        // 1.1、 在控制台打印请求参数
        client.setPrintParam(true);
        // 2、 创建 API 对象; 如果对 Aria2 规范很熟悉, 也可以不使用API对象, 直接使用 client的 call方法发送请求
        final Aria2Api api = new Aria2Api(client);
        // 3、 调用 API 对象的方法 获得 Aria2MethodCall 对象
        final Aria2MethodCall<Aria2Version> versionCall = api.getVersion();
        // 4、 Aria2MethodCall 对象 可以选择 异步(async) 还是 同步(sync) 方法发送请求; 以String结尾的方法会返回json字符串结果, 需要自行解析为实体对象
        // 4.1 同步
        try {
            JsonUtils.printJson(versionCall.sync());
        } catch (IOException | Aria2RequestException e) {
            throw new RuntimeException(e);
        }
        // 4.2 异步
        JsonUtils.printJson(api.getGlobalOption().sync());
    }

    private static final int TIMEOUT = 5000;//超时时间设置为5s，实际并不需要这么久
    private static final String IP = "100.122.250.55";//要ping的ip

    public static boolean isPing() {
        boolean status = false;
        if (IP != null) {
            try {
                status = InetAddress.getByName(IP).isReachable(TIMEOUT);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ping "+ IP +"的返回结果为: " + status);
        return status;
    }
}
