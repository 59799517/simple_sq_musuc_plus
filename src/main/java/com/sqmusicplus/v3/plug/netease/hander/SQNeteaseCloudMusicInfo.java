package com.sqmusicplus.v3.plug.netease.hander;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.utils.DownloadUtils;

import java.util.HashMap;


/**
 * @Classname SQNeteaseCloudMusicInfo
 * @Description NeteaseCloudMusicInfo扩展
 * @Version 1.0.0
 * @Date 2025/7/11 09:06
 * @Created by SQ
 */

public class SQNeteaseCloudMusicInfo {
    private String baseUrl="";
    private String cookie="";
    private HashMap<String, String> header = new HashMap<>();


    /**
     * 初始化并解析URL中的###key:value###特殊设置
     * <p>
     * 支持的格式：
     * <ul>
     *   <li>单header纯文本：http://example.com###Key:value###</li>
     *   <li>多header纯文本：http://example.com###Key1:val1;Key2:val2###</li>
     *   <li>Base64编码值：http://example.com###Key:base64:5YC8###</li>
     *   <li>混合模式：http://example.com###Key1:val1;Key2:base64:5YC8###</li>
     * </ul>
     */
    public void init(String baseUrl){
        this.header.clear();
        this.cookie = "";
        if (baseUrl == null) {
            this.baseUrl = "";
            return;
        }

        // 逐步解析URL中的###...###块，处理相邻(####)情况
        String url = baseUrl;
        int idx = 0;
        while (true) {
            int openIdx = url.indexOf("###", idx);
            if (openIdx == -1) break;
            int closeIdx = url.indexOf("###", openIdx + 3);
            if (closeIdx == -1) break;

            // 提取###...###之间的内容
            String content = url.substring(openIdx + 3, closeIdx);
            parseHeaderBlock(content);

            // 移除这个###...###块，继续解析
            url = url.substring(0, openIdx) + url.substring(closeIdx + 3);
            // idx保持在原位（后续内容前移，无需调整）
        }

        this.baseUrl = url;
    }

    /**
     * 解析###...###块中的header配置
     * <p>
     * 支持分号分隔多个key:value对，值支持Base64编码
     * <pre>
     *   Key1:val1;Key2:val2           → 纯文本值，分号分隔
     *   Key1:base64:5YC85LiA5Liq      → Base64编码的值
     *   Key1:val1;Key2:base64:5YC8    → 混合模式
     * </pre>
     */
    private void parseHeaderBlock(String content) {
        // 先按分号分割多个key:value对
        String[] pairs = content.split(";");
        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isEmpty()) continue;

            int colonIdx = pair.indexOf(':');
            if (colonIdx <= 0) continue;

            String key = pair.substring(0, colonIdx).trim();
            String valuePart = pair.substring(colonIdx + 1).trim();

            // 检查是否是Base64编码的值
            String value;
            if (valuePart.startsWith("base64:")) {
                String base64Str = valuePart.substring(7).trim();
                try {
                    value = new String(java.util.Base64.getDecoder().decode(base64Str));
                } catch (Exception e) {
                    // Base64解码失败时，保留原始base64字符串
                    value = base64Str;
                }
            } else {
                value = valuePart;
            }

            if (!key.isEmpty()) {
                header.put(key, value);
            }
        }
    }

    public void init(String baseUrl,String cookie){
        this.baseUrl = baseUrl;
        this.cookie = cookie;
    }

    public void init(String baseUrl, String cookie, HashMap<String, String> header) {
        this.baseUrl = baseUrl;
        this.cookie = cookie;
        this.header = header;
    }

    public JSONObject songMusicDetail(JSONObject parameter) {
        String url = "/song/music/detail?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }


    public JSONObject songDownloadUrl(JSONObject parameter) {
        String url = "/song/download/url?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);

    }


    public JSONObject searchSuggest(JSONObject parameter) {
        String url = "/search/suggest?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }



    public JSONObject cloudsearch(JSONObject parameter) {
        String url = "/cloudsearch?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }



    public JSONObject songDetail(JSONObject parameter) {
        String url = "/song/detail?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }

    public JSONObject artistDetail(JSONObject parameter) {
        String url = "/artist/detail?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }


    public JSONObject album(JSONObject parameter) {
        String url = "/album?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }



    public JSONObject lyric(JSONObject parameter) {
        String url = "/lyric?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }


    public JSONObject artistAlbum(JSONObject parameter) {
        String url = "/artist/album?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }

    public JSONObject playlistDetail(JSONObject parameter) {
        String url = "/playlist/detail?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }

    public JSONObject playlistTrackAll(JSONObject parameter) {
        String url = "/playlist/track/all?_t=" + System.currentTimeMillis();
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }
    public JSONObject innerVersion(){
        String url = "/inner/version";
        return DownloadUtils.getToJsonObject(baseUrl + url);
    }
    public JSONObject songWikiSummary(JSONObject parameter){
        String url = "/song/wiki/summary";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie,header);
    }



    public String getBaseUrl() {
        return baseUrl;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }
}
