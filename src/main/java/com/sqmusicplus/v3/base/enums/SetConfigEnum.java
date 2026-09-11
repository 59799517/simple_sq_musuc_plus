package com.sqmusicplus.v3.base.enums;

/**
 * @Classname SetConfigEnum
 * @Description 设置配置类
 * @Version 1.0.0
 * @Date 2025/7/14 10:35
 * @Created by SQ
 */

public enum SetConfigEnum {
    /**
     * 系统下载路径
     */
    SYSTEM_DOWNLOAD_PATH("system.download.path","音乐下载路径","system"),
    /**
     * 是否忽略伴奏
     */
    SYSTEM_IGNORE_ACCOMPANIMENT("system.ignore.accompaniment","是否忽略伴奏片段等格式","system"),
    /**
     * 伴奏正则表达式
     */
    SYSTEM_IGNORE_ACCOMPANIMENT_EXPRESSION("system.ignore.accompaniment.expression","伴奏表达式多个|（管道符）分割","system"),
    /**
     * 文件存在时不下载
     */
    SYSTEM_FILE_EXIST_NOT_DOWNLOAD("system.file.exist.not.download","文件存在时不下载","system"),
    /**
     * 同时下载的数量
     */
    SYSTEM_DOWNLOAD_NUM("system.download.num","同时下载的数量","system"),
    /**
     * 登录账号
     */
    SYSTEM_LOGIN_ACCOUNT("system.login.account","登录账号","system"),
    /**
     * 登录密码
     */
    SYSTEM_LOGIN_PASSWORD("system.login.password","登录密码","system"),
    /**
     * 开启降级下载
     */
    SYSTEM_DOWNLOAD_DEGRADE("system.download.degrade","开启降级下载","system"),
    /**
     * 排除的歌单名称多个|（管道符）分割
     */
    SYSTEM_SYNC_PLAYLIST_EXCLUDE("system.sync.playlist.exclude","排除的歌单名称多个|（管道符）分割","system"),
    /**
     * 排除的专辑名称多个|（管道符）分割
     */
    SYSTEM_SYNC_ALBUM_EXCLUDE("system.sync.album.exclude","排除的专辑名称多个|（管道符）分割","system"),
    /**
     * 排除的歌手名称多个|（管道符）分割
     */
    SYSTEM_SYNC_ARTISTS_EXCLUDE("system.sync.artists.exclude","排除的歌手名称多个|（管道符）分割","system"),

    /**
     * 显示播放连接（下载链接）
     */
    SYSTEM_SHOW_PLAY_URL("system.show.play.url","是否显示播放连接（下载链接）","system"),

    /**
     * 开始文件夹和文件的特殊符号移除（百度网盘上传）
     */
    SYSTEM_START_FILE_AND_FOLDER_SPECIAL_SYMBOL_REMOVE("system.start.file.and.folder.special.symbol.remove","开始文件夹和文件的特殊符号移除（百度网盘上传）","system"),
    /**
     * 需要移除的特护符号
     */
    SYSTEM_START_FILE_AND_FOLDER_SPECIAL_SYMBOL_REMOVE_SYMBOL("system.start.file.and.folder.special.symbol.remove.symbol","需要移除的特护符号","system"),

    /**
     * 下载文件模板
     */
    SYSTEM_DOWNLOAD_FILE_TEMPLATE("system.download.file.template","下载文件名称模板","system"),
    /**
     * 下载音频格式
     */
    SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT("system.download.file.audio.format","下载文件音频格式","system"),
    /**
     * 流量监控显示
     */
    SYSTEM_SHOW_TRAFFIC_MONITORING("system.show.traffic.monitoring","流量监控显示","system"),
    /**
     * 是否启用下载失败歌曲使用其他插件代替下载
     */
    SYSTEM_DOWNLOAD_FAILED_USE_OTHER_PLUGIN("system.download.failed.use.other.plugin","是否启用下载失败歌曲使用其他插件代替下载","system"),
    /**
     * 下载失败时使用其他插件的顺序（逗号分隔的插件名称）
     */
    SYSTEM_DOWNLOAD_FAILED_USE_OTHER_PLUGIN_ORDER("system.download.failed.use.other.plugin.order","下载失败时使用其他插件的顺序","system"),
    /**
     * 下载失败时使用其他插件搜索歌曲的匹配模式
     * strict — 严格匹配：名称+歌手+专辑完全一致
     * name_artist_alubm_like — 名称+歌手+专辑名称包含即可
     * name_artist_fuzzy — 名称+歌手包含匹配（推荐默认值）
     * name_fuzzy — 仅名称包含匹配（最宽松，不建议）
     */
    SYSTEM_DOWNLOAD_FAILED_USE_OTHER_PLUGIN_MATCH_MODE("system.download.failed.use.other.plugin.match.mode","下载失败时使用其他插件搜索歌曲的匹配模式","system"),
    /**
     * 下载超时歌曲重试间隔时长（分）
     */
    SYSTEM_DOWNLOAD_TIMEOUT_RETRY_INTERVAL("system.download.timeout.retry.interval","下载超时歌曲重试间隔时长（分）","system"),
    /**
     * 插件下载超时重试次数
     */
    SYSTEM_DOWNLOAD_TIMEOUT_RETRY_NUM("system.download.timeout.retry.num","插件下载超时重试次数","system"),

//    ------------QQ插件配置-----------------


    /**
     * 是否开启QQvip音乐插件
     */
    PLUG_QQVIP_OPEN("plug.qqvip.open","是否开启QQ音乐插件","plug"),
    /**
     * 是否开启QQ音乐自动刷新登录信息
     */
    PLUG_QQVIP_AUTO_REFRESH_LOGIN("plug.qqvip.auto.refresh.login","是否开启QQ音乐自动刷新登录信息","plug"),
    /**
     * 是否开启qq音乐自动下载我喜欢音乐内容
     */
    PLUG_QQVIP_SYNC_MY_LIKE_MUSIC("plug.qqvip.sync.my.like.music","是否开启QQ音乐自动下载我喜欢音乐内容","plug"),
    /**
     * 是否开启QQ音乐自动下载我喜欢专辑内容
     */
    PLUG_QQVIP_SYNC_MY_LIKE_ALBUM("plug.qqvip.sync.my.like.album","是否开启QQ音乐自动下载我喜欢专辑内容","plug"),
    /**
     * 是否开启QQ音乐自动下载我喜欢歌手内容
     */
    PLUG_QQVIP_SYNC_MY_LIKE_ARTISTS("plug.qqvip.sync.my.like.artists","是否开启QQ音乐自动下载我喜欢歌手内容","plug"),
    /**
     * 是否开启QQ音乐自动下载我喜欢歌单内容
     */
    PLUG_QQVIP_SYNC_MY_LIKE_PLAYLIST("plug.qqvip.sync.my.like.playlist","是否开启QQ音乐自动下载我喜欢歌单内容","plug"),

    /**
     * QQ音乐cookie
     */
    PLUG_QQVIP_COOKIE("plug.qqvip.cookie","QQ音乐cookie","plug"),
    /**
     * QQ音乐qrcode
     */
    PLUG_QQVIP_QRCODE("plug.qqvip.qrcode","QQ音乐qrcode","plug"),
    /**
     * QQ音乐每日最大下载限额
     */
    PLUG_QQVIP_DOWNLOAD_DAILY_LIMIT("plug.qqvip.download.daily.limit","QQ音乐每日最大下载限额","plug"),
    /**
     * QQ音乐今日下载
     */
    PLUG_QQVIP_DOWNLOAD_TODAY("plug.qqvip.download.today","QQ音乐今日下载","plug"),
    /**
     * 歌曲tag标签数据来源
     */
    PLUG_QQVIP_TAG_SOURCE("plug.qqvip.tag.source","歌曲tag标签数据来源","plug"),

//    -----------------网易云音乐插件配置-----------------
    /**
     * 是否开启网易云音乐插件
     */
    PLUG_NETEASE_OPEN("plug.netease.open","是否开启网易云音乐插件","plug"),

    /**
     * 网易云音乐API地址
     */
    PLUG_NETEASE_BASEURL("plug.netease.baseurl","网易云音乐API地址","plug"),

    /**
     * 网易云音乐用户信息
     */
    PLUG_NETEASE_COOKIEURL("plug.netease.cookieurl","网易云音乐API地址url","plug"),

    /**
     * 网易云音乐用户信息
     */
    PLUG_NETEASE_COOKIE("plug.netease.cookie","网易云音乐用户信息","plug"),
    /**
     * 是否使用扩展下载
     */
    PLUG_NETEASE_EXTEND_DOWNLOAD("plug.netease.extend.download","是否使用扩展下载","plug"),
    /**
     * 网易云扩展下载地址
     */
    PLUG_NETEASE_EXTEND_DOWNLOAD_URL("plug.netease.extend.download.url","网易云音乐扩展下载地址","plug"),
    /**
     * 歌曲tag标签数据来源
     */
    PLUG_NETEASE_TAG_SOURCE("plug.netease.tag.source","歌曲tag标签数据来源","plug"),

//    ------------------酷狗概念音乐插件配置-----------------
    /**
     * 是否开启酷狗念音乐插件
     */
    PLUG_KG_OPEN("plug.kg.open","是否开启酷狗插件","plug"),
    /**
     * 酷狗概念API地址
     */
    PLUG_KG_BASEURL("plug.kg.baseurl","酷狗概念API地址","plug"),

    /**
     * 酷狗用户信息
     */
    PLUG_KG_USER_INFO("plug.kg.user.info","酷狗用户信息","plug"),
    /**
     * 酷狗二维码信息
     */
    PLUG_KG_QRCODE_INFO("plug.kg.qrcode.code","酷狗二维码信息","plug"),

    /**
     * 酷狗微信二维码信息
     */
    PLUG_KG_QRCODE_WX_CODE("plug.kg.qrcode.wx.code","酷狗微信二维码信息","plug"),
    /**
     * 是否开启酷狗念音乐自动下载我收藏的歌单内容
     */
    PLUG_KG_SYNC_MY_COLLECT_PLAYLIST("plug.kg.sync.my.collect.playlist","是否开启酷狗念音乐自动下载我收藏的歌单内容","plug"),
    /**
     * 是否开启酷狗念音乐自动签到
     */
    PLUG_KG_SIGN_OPEN("plug.kg.sign.open","是否开启酷狗念音乐自动签到","plug"),
    /**
     * 酷狗最后一次自动签到时间
     */
    PLUG_KG_SIGN_LAST_TIME("plug.kg.sign.last.time","酷狗最后一次自动签到时间","plug"),
    /**
     * 酷狗 concept sign begin-end time
     */

    PLUG_KG_SIGN_BEGIN_END_TIME("plug.kg.sign.begin-end.time","酷狗概念签到到期信息","plug"),

    /**
     * 歌曲tag标签数据来源
     */
    PLUG_KG_TAG_SOURCE("plug.kg.tag.source","歌曲tag标签数据来源","plug"),
//    ------------------酷我插件配置-----------------
    /**
     * 是否开启酷我插件
     */
    PLUG_KW_OPEN("plug.kw.open","是否开启酷我插件","plug"),
    /**
     * 歌曲tag标签数据来源
     */
    PLUG_KW_TAG_SOURCE("plug.kw.tag.source","歌曲tag标签数据来源","plug"),



    //    ------------------咪咕插件配置-----------------
    /**
     * 是否开启酷我插件
     */
    PLUG_MG_OPEN("plug.mg.open","是否开启酷我插件","plug"),
    /**
     * 歌曲tag标签数据来源
     */
    PLUG_MG_TAG_SOURCE("plug.mg.tag.source","歌曲tag标签数据来源","plug"),

// --------------------------freemp3------------------
    PLUG_FREEMP3_OPEN("plug.freemp3.open","是否开启freemp3插件","plug"),



//------------------------------apple--------------------

    /**
     * 是否开启apple插件
     */
    PLUG_APPLE_OPEN("plug.apple.open","是否开启apple插件","plug"),
    /**
     * storefront代码
     * 由 ISO 3166 alpha-2 国家/地区代码指定的 iTunes Store 地区。可能的值是 Store front 对象的 id 属性
     */
    PLUG_APPLE_STOREFRONT("plug.apple.storefront","storefront代码 由 ISO 3166 alpha-2 国家/地区代码指定的 iTunes Store 地区。可能的值是 Store front 对象的 id 属性","plug"),
    /**
     * token
     */
    PLUG_APPLE_TOKEN("plug.apple.token","authorization 下的token","plug"),
    /**
     * cookie值
     */
    PLUG_APPLE_COOKIE("plug.apple.cookie","cookie值json格式","plug"),
    /**
     * 媒体用户token
     */
    PLUG_APPLE_MEDIAUSERTOKEN("plug.apple.mediausertoken","媒体用户token（无需修改根据cookie解析生成）","plug"),
    /**
     * apple 请求头  origin地址
     */
    PLUG_APPLE_ORIGIN("plug.apple.origin","apple 请求头  origin地址","plug"),




    //--------------------------tidal--------------------
    PLUG_TIDAL_OPEN("plug.tidal.open","是否开启tidal插件","plug"),
    /**
     * tidal token
     */
    PLUG_TIDAL_TOKEN("plug.tidal.token","tidal token","plug"),



    /**
     * 是否启用阿里云盘功能
     */
    EXPAND_ALIYUN_OPEN("expand.aliyun.open","是否开启阿里云盘插件","expand"),

    /**
     * 阿里云盘的appid
     */
    EXPAND_ALIYUN_APPID("expand.aliyun.appid","阿里云盘的appid","expand"),
    /**
     * 阿里云盘的appsecret
     */
    EXPAND_ALIYUN_APPSECRET("expand.aliyun.appsecret","阿里云盘的appsecret","expand"),
    /**
     * 阿里云盘的refresh_token
     */
    EXPAND_ALIYUN_REFRESH_TOKEN("expand.aliyun.refresh_token","阿里云盘的refresh_token","expand"),
    /**
     * 阿里圆盘的access_token
     */
    EXPAND_ALIYUN_ACCESS_TOKEN("expand.aliyun.access_token","阿里云盘的access_token","expand"),
    /**
     * 阿里云盘的code_verifier
     */
    EXPAND_ALIYUN_CODE_VERIFIER("expand.aliyun.code_verifier","阿里云盘的code_verifier","expand"),
    /**
     * 阿里云盘的授权码
     */
    EXPAND_ALIYUN_CODE("expand.aliyun.code","阿里云盘的授权码","expand"),
    /**
     * 阿里云盘的用户名称
     */
    EXPAND_ALIYUN_USER_NAME("expand.aliyun.user.name","阿里云盘的用户名称","expand"),
    /**
     * 阿里云盘的头像
     */
    EXPAND_ALIYUN_AVATAR("expand.aliyun.avatar","阿里云盘的头像","expand"),
    /**
     * 阿里云盘的user_id
     */
    EXPAND_ALIYUN_USER_ID("expand.aliyun.user.id","阿里云盘的user_id","expand"),
    /**
     * 阿里云盘的账号名称
     */
    EXPAND_ALIYUN_USER_INFO_NAME("expand.aliyun.user.info.name","阿里云盘的账号名称","expand"),
    /**
     * 阿里云盘的昵称
     */
    EXPAND_ALIYUN_NICK_NAME("expand.aliyun.nick.name","阿里云盘的昵称","expand"),
    /**
     * 阿里云盘的backup_drive_id
     */
    EXPAND_ALIYUN_BACKUP_DRIVE_ID("expand.aliyun.backup.drive_id","阿里云盘的backup_drive_id","expand"),
    /**
     * 阿里云盘的resource_drive_id
     */
    EXPAND_ALIYUN_RESOURCE_DRIVE_ID("expand.aliyun.resource.drive_id","阿里云盘的resource_drive_id","expand"),
    /**
     * 存储到阿里云盘的目录位置
     */
    EXPAND_ALIYUN_FOLDER_PATH("expand.aliyun.folder.path","存储到阿里云盘的目录位置","expand"),
    /**
     * 同步模式
     */
    EXPAND_ALIYUN_SYNC_MODE("expand.aliyun.sync.mode","同步模式","expand"),
    /**
     * 阿里云盘access_token到期时间
     */
    EXPAND_ALIYUN_ACCESS_TOKEN_EXPIRE_TIME("expand.aliyun.access_token.expire.time","阿里云盘access_token到期时间","expand");










    /**
     * 配置的key
     */
    private String key;
    /**
     * 配值描述
     */
    private String describe;
    /**
     * 配置类型
     */
    private String type;

    SetConfigEnum(String key, String describe, String type) {
        this.key = key;
        this.describe = describe;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getDescribe() {
        return describe;
    }

    public String getType() {
        return type;
    }
}
