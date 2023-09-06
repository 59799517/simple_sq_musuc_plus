package com.sqmusicplus.plug.aria2.aria2.main;

import com.sqmusicplus.plug.aria2.aria2.call.Aria2MethodCall;
import com.sqmusicplus.plug.aria2.aria2.dto.base.Aria2Option;
import com.sqmusicplus.plug.aria2.aria2.dto.base.Aria2Param;
import com.sqmusicplus.plug.aria2.aria2.dto.form.Aria2AddUriForm;
import com.sqmusicplus.plug.aria2.aria2.enums.Aria2Method;
import com.sqmusicplus.plug.aria2.aria2.exception.Aria2RequestException;
import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2ResponseMultiString;
import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2ResponseString;
import com.sqmusicplus.plug.aria2.aria2.response.result.Aria2GlobalOption;
import com.sqmusicplus.plug.aria2.aria2.response.result.Aria2GlobalStatus;
import com.sqmusicplus.plug.aria2.aria2.response.result.Aria2TaskStatus;
import com.sqmusicplus.plug.aria2.aria2.response.result.Aria2Version;
import lombok.Getter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * api <a href="https://aria2.github.io/manual/en/html/aria2c.html">API文档</a>
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/15 15:57
 */
@Getter
public class Aria2Api {
    final Aria2Client client;

    public Aria2Api(Aria2Client client) {
        this.client = client;
    }

    /**
     * 添加单个任务
     * @param form 表单
     */
    public Aria2MethodCall<String> addUri(Aria2AddUriForm form) {
        return client.call(form.buildParam(), Aria2ResponseString.class);
    }

    /**
     * 批量添加任务
     * @param forms 表单
     * @return gid
     */
    public Aria2MethodCall<List<List<String>>> addUris(Collection<Aria2AddUriForm> forms) {
        final List<Aria2Param> params = forms.stream().map(Aria2AddUriForm::buildParam).collect(Collectors.toList());
        return client.call(params, Aria2ResponseMultiString.class);
    }

    /**
     * 批量添加任务，每个url为一个任务，使用相同下载参数
     * @param form 表单
     * @return gid
     */
    public Aria2MethodCall<List<List<String>>> addUris(Aria2AddUriForm form) {
        return addUris(form.getUrls().stream()
                               .map(url -> new Aria2AddUriForm(Collections.singleton(url), form.getParams().clone()))
                               .collect(Collectors.toList())
        );
    }

    /**
     * 获取概况统计
     * @return 概况统计
     */
    public Aria2MethodCall<Aria2GlobalStatus> getGlobalStat() {
        return client.call(new Aria2Param(Aria2Method.getGlobalStat), Aria2GlobalStatus.Response.class);
    }

    /**
     * 获取下载参数
     * @param gid gid
     * @return 下载参数
     */
    public Aria2MethodCall<Aria2Option> getOption(String gid) {
        return client.call(new Aria2Param(Aria2Method.getOption, gid), Aria2Option.Response.class);
    }

    /**
     * 批量获取下载参数
     * @param gid gid
     * @return 下载参数
     */
    public Aria2MethodCall<List<List<Aria2Option>>> getOption(Collection<String> gid) {
        return client.call(Aria2Param.listOf(Aria2Method.getOption, gid), Aria2Option.ResMulti.class);
    }

    /**
     * 获取全局配置
     * @return 全局配置
     */
    public Aria2MethodCall<Aria2GlobalOption> getGlobalOption() {
        return client.call(new Aria2Param(Aria2Method.getGlobalOption), Aria2GlobalOption.Response.class);
    }

    /**
     * 获取版本信息
     * @return 版本信息
     */
    public Aria2MethodCall<Aria2Version> getVersion() {
        return client.call(new Aria2Param(Aria2Method.getVersion), Aria2Version.Response.class);
    }

    /**
     * 移除下载中的任务
     * @param gid gid
     * @return gid
     */
    public Aria2MethodCall<String> remove(String gid) {
        return client.call(new Aria2Param(Aria2Method.remove, gid), Aria2ResponseString.class);
    }

    /**
     * 批量移除下载中的任务
     * @param gid gid
     * @return gid
     */
    public Aria2MethodCall<List<List<String>>> remove(Collection<String> gid) {
        return client.call(Aria2Param.listOf(Aria2Method.remove, gid), Aria2ResponseMultiString.class);
    }

    /**
     * 移除停止任务
     * @param gid gid
     * @return gid
     */
    public Aria2MethodCall<String> removeDownloadResult(String gid) {
        return client.call(new Aria2Param(Aria2Method.removeDownloadResult, gid), Aria2ResponseString.class);
    }

    /**
     * 批量移除停止任务
     * @param gid gid
     * @return gid
     */
    public Aria2MethodCall<List<List<String>>> removeDownloadResult(Collection<String> gid) {
        return client.call(Aria2Param.listOf(Aria2Method.removeDownloadResult, gid), Aria2ResponseMultiString.class);
    }

    /**
     * 查询活动任务
     * @param keys <a href="https://aria2.github.io/manual/en/html/aria2c.html#aria2.tellStatus">字段</a>
     * @return 任务状态
     */
    public Aria2MethodCall<List<Aria2TaskStatus>> tellActive(String... keys) {
        return client.call(new Aria2Param(Aria2Method.tellActive, Arrays.asList(keys)), Aria2TaskStatus.ResponseList.class);
    }

    /**
     * 查询所有任务
     * @param page 页数
     * @param size 每页条数
     * @param keys <a href="https://aria2.github.io/manual/en/html/aria2c.html#aria2.tellStatus">字段</a>
     * @return 任务状态
     */
    public Aria2MethodCall<List<List<List<Aria2TaskStatus>>>> tellAll(int page, int size, String... keys) {
        final Aria2Param param1 = new Aria2Param(Aria2Method.tellActive, Arrays.asList(keys));
        final Aria2Param param2 = new Aria2Param(Aria2Method.tellWaiting, page, size, keys);
        final Aria2Param param3 = new Aria2Param(Aria2Method.tellStopped, page, size, keys);
        return client.call(Arrays.asList(param1, param2, param3), Aria2TaskStatus.ResMultiList.class);
    }

    /**
     * 查询队列中的任务(tellActive+tellWaiting)
     * @param page 页数
     * @param size 每页条数
     * @param keys <a href="https://aria2.github.io/manual/en/html/aria2c.html#aria2.tellStatus">字段</a>
     * @return 任务状态
     */
    public Aria2MethodCall<List<List<List<Aria2TaskStatus>>>> tellQueue(int page, int size, String... keys) {
        final Aria2Param param1 = new Aria2Param(Aria2Method.tellActive, Arrays.asList(keys));
        final Aria2Param param2 = new Aria2Param(Aria2Method.tellWaiting, page, size, keys);
        return client.call(Arrays.asList(param1, param2), Aria2TaskStatus.ResMultiList.class);
    }

    /**
     * 查询单个任务状态
     * @param gid  gid
     * @param keys <a href="https://aria2.github.io/manual/en/html/aria2c.html#aria2.tellStatus">字段</a>
     * @return 任务状态
     */
    public Aria2MethodCall<Aria2TaskStatus> tellStatus(String gid, String... keys) {
        return client.call(new Aria2Param(Aria2Method.tellStatus, Arrays.asList(gid, Arrays.asList(keys))), Aria2TaskStatus.Response.class);
    }

    /**
     * 查询多个任务状态
     * @param gid  gid
     * @param keys <a href="https://aria2.github.io/manual/en/html/aria2c.html#aria2.tellStatus">字段</a>
     * @return 任务状态
     */
    public Aria2MethodCall<List<List<Aria2TaskStatus>>> tellStatus(Collection<String> gid, String... keys) {
        return client.call(gid.stream().map(g -> new Aria2Param(Aria2Method.tellStatus,
                                                                Arrays.asList(g, Arrays.asList(keys)))).collect(Collectors.toList()),
                           Aria2TaskStatus.ResMulti.class);
    }

    /**
     * 查询停止任务
     * @param page 页数
     * @param size 每页条数
     * @param keys <a href="https://aria2.github.io/manual/en/html/aria2c.html#aria2.tellStatus">字段</a>
     * @return 任务状态
     */
    public Aria2MethodCall<List<Aria2TaskStatus>> tellStop(int page, int size, String... keys) {
        return client.call(new Aria2Param(Aria2Method.tellStopped, page, size, keys), Aria2TaskStatus.ResponseList.class);
    }

    /**
     * 查询等待任务
     * @param page 页数
     * @param size 每页条数
     * @param keys <a href="https://aria2.github.io/manual/en/html/aria2c.html#aria2.tellStatus">字段</a>
     * @return 任务状态
     */
    public Aria2MethodCall<List<Aria2TaskStatus>> tellWaiting(int page, int size, String... keys) {
        return client.call(new Aria2Param(Aria2Method.tellWaiting, page, size, keys), Aria2TaskStatus.ResponseList.class);
    }

    /**
     * 将一个停止任务删除重试
     * @param gid gid
     */
    public String retry(String gid) throws Aria2RequestException, IOException {
        return retry(tellStatus(gid).sync());
    }
    /**
     * 将一个停止任务删除重试
     * @param taskStatus 任务状态
     */
    private String retry(Aria2TaskStatus taskStatus) throws IOException, Aria2RequestException {
        // 下载地址
        final List<String> urls = taskStatus.getUrls();
        // 下载参数
        final Aria2Option option = getOption(taskStatus.getGid()).sync();
        // 新建任务
        final String res = addUri(new Aria2AddUriForm(urls, option)).sync();
        // 删除旧任务
        removeDownloadResult(taskStatus.getGid()).sync();
        return res;
    }
    /**
     * 批量重试任务
     * @param gid gid
     * @return 旧gid到新gid的映射
     */
    public HashMap<String ,String > retryGid(Collection<String> gid) throws IOException, Aria2RequestException {
        return retryTask(tellStatus(gid,"gid","files").sync().stream().flatMap(Collection::stream).collect(Collectors.toList()));
    }
    /**
     * 批量重试任务
     * @param taskStatus 任务状态
     * @return 旧gid到新gid的映射
     */
    public HashMap<String ,String > retryTask(Collection<Aria2TaskStatus> taskStatus) throws IOException, Aria2RequestException {
        final List<String> gidList = taskStatus.stream().map(Aria2TaskStatus::getGid).collect(Collectors.toList());
        // 获取下载参数
        final List<Aria2Option> options = getOption(gidList).sync().stream().flatMap(Collection::stream).collect(Collectors.toList());

        // 构建请求参数
        final List<Aria2AddUriForm> forms = new ArrayList<>();
        for (int i = 0; i < taskStatus.size(); i++) {
            final String gid = gidList.get(i);
            final Aria2TaskStatus task = taskStatus.stream().filter(t -> t.getGid().equals(gid)).findFirst().orElse(null);
            final Aria2Option option = options.get(i);
            if (task != null && option != null) {
                forms.add(new Aria2AddUriForm(task.getUrls(), option));
            }
        }
        // 批量发送请求
        final List<String> resGid = addUris(forms).sync().stream().flatMap(Collection::stream).collect(Collectors.toList());

        // 删除旧任务
        removeDownloadResult(gidList).sync();

        final HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < gidList.size(); i++) {
            final String key = gidList.get(i);
            final String value = resGid.get(i);
            map.put(key,value);
        }
        return map;
    }

}
