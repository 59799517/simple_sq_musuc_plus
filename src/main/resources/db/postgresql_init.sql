-- PostgreSQL 版本的数据库初始化脚本（全量）
-- 等价于 MySQL 链路 sqlInit.sql + sq_config.sql + 3.0.17 ~ 3.1.30 全部升级脚本的最终状态
-- 说明：
--   1. 由 AutoDdlConfig 在检测到 jdbc:postgresql 连接时执行，ddl_history 记录后不会重复执行；
--   2. 全部语句幂等（IF NOT EXISTS / ON CONFLICT DO NOTHING），部分执行失败后可安全重跑；
--   3. 含反斜杠转义的字符串使用 E'' 转义字符串语法，语义与 MySQL 保持一致（\r\n 换行、\" 引号、\\ 反斜杠）。

-- ==================== 表结构 ====================

CREATE TABLE IF NOT EXISTS download_info (
    id SERIAL PRIMARY KEY,
    download_gid VARCHAR(255) DEFAULT NULL,
    download_time TIMESTAMP DEFAULT NULL,
    download_file VARCHAR(1000) DEFAULT NULL,
    download_music_id VARCHAR(255) NOT NULL,
    download_plug_name VARCHAR(255) DEFAULT NULL,
    download_br_type VARCHAR(255) DEFAULT NULL,
    download_musicname VARCHAR(255) DEFAULT NULL,
    download_artistname VARCHAR(255) DEFAULT NULL,
    download_albumname VARCHAR(255) DEFAULT NULL,
    download_msg VARCHAR(255) DEFAULT NULL,
    version VARCHAR(255) DEFAULT NULL,
    download_music_info TEXT DEFAULT NULL,
    download_status VARCHAR(255) DEFAULT NULL,
    spring_name VARCHAR(255) DEFAULT NULL,
    audio_book INTEGER DEFAULT NULL,
    download_update_time TIMESTAMP DEFAULT NULL,
    revision INTEGER DEFAULT NULL,
    rewrite_mp3tag INTEGER DEFAULT NULL,
    download_bits VARCHAR(255) DEFAULT NULL,
    download_br_types VARCHAR(255) DEFAULT NULL,
    parent_download_id INTEGER DEFAULT NULL,
    download_retry_num INTEGER DEFAULT NULL,
    download_retry_time TIMESTAMP DEFAULT NULL
);

CREATE INDEX IF NOT EXISTS idx_download_albumname ON download_info (download_albumname);
CREATE INDEX IF NOT EXISTS idx_download_artistname ON download_info (download_artistname);
CREATE INDEX IF NOT EXISTS idx_download_musicname ON download_info (download_musicname);

CREATE TABLE IF NOT EXISTS sq_config (
    config_id SERIAL PRIMARY KEY,
    config_name VARCHAR(255) DEFAULT NULL,
    config_value TEXT DEFAULT NULL,
    config_key VARCHAR(255) DEFAULT NULL,
    config_type VARCHAR(255) DEFAULT NULL,
    config_options TEXT DEFAULT NULL,
    config_show INTEGER DEFAULT NULL,
    config_remark VARCHAR(1000) DEFAULT NULL,
    config_null_check INTEGER DEFAULT NULL,
    config_disabled INTEGER DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS sq_sync (
    id SERIAL PRIMARY KEY,
    plug_name VARCHAR(255) DEFAULT NULL,
    music_id VARCHAR(255) DEFAULT NULL,
    music_info TEXT DEFAULT NULL,
    download_id INTEGER DEFAULT NULL,
    play_list_name VARCHAR(255) DEFAULT NULL,
    play_list_sha1 VARCHAR(255) DEFAULT NULL,
    play_list_id VARCHAR(255) DEFAULT NULL,
    artist_id VARCHAR(255) DEFAULT NULL,
    artist_name VARCHAR(255) DEFAULT NULL,
    album_id VARCHAR(255) DEFAULT NULL,
    album_name VARCHAR(255) DEFAULT NULL,
    music_name VARCHAR(500) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS sq_monitor (
    id SERIAL PRIMARY KEY,
    plug_name VARCHAR(255) DEFAULT NULL,
    type VARCHAR(255) DEFAULT NULL,
    enabled VARCHAR(255) DEFAULT NULL,
    target_id VARCHAR(255) DEFAULT NULL,
    target_name TEXT DEFAULT NULL,
    target_desc TEXT DEFAULT NULL,
    target_url TEXT DEFAULT NULL,
    target_count INTEGER DEFAULT NULL,
    target_cover TEXT DEFAULT NULL,
    target_update_time BIGINT DEFAULT NULL,
    create_time TIMESTAMP DEFAULT NULL,
    update_time TIMESTAMP DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS sq_ali_sync (
    id SERIAL PRIMARY KEY,
    sha1 VARCHAR(255) DEFAULT NULL,
    md5 VARCHAR(255) DEFAULT NULL,
    sharding_sha1 VARCHAR(255) DEFAULT NULL,
    path TEXT DEFAULT NULL,
    ali_id_path TEXT DEFAULT NULL,
    ali_path TEXT DEFAULT NULL,
    name VARCHAR(255) DEFAULT NULL,
    music_name VARCHAR(255) DEFAULT NULL,
    music_artist VARCHAR(255) DEFAULT NULL,
    music_album VARCHAR(255) DEFAULT NULL,
    suffix VARCHAR(255) DEFAULT NULL,
    upload_time TIMESTAMP DEFAULT NULL,
    result VARCHAR(255) DEFAULT NULL,
    rapid INTEGER DEFAULT NULL,
    download_id INTEGER DEFAULT NULL
);

-- ==================== 初始配置数据（sq_config） ====================
-- config_id 与 MySQL 全新安装后的最终状态保持一致

INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (1, '音乐下载路径', '/music', 'system.download.path', 'path', NULL, 1, '音乐下载路径', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (2, '是否忽略伴奏片段等格式', 'true', 'system.ignore.accompaniment', 'boolean', NULL, 1, '例如 伴奏，片段等', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (3, '文件存在时不下载', 'true', 'system.file.exist.not.download', 'boolean', NULL, 1, '文件存在时不下载', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (4, '同时下载的数量', '8', 'system.download.num', 'number', NULL, 1, '允许同时下载歌的数量（最小是1）越大消耗的资源越多也有可能碰到封IP', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (5, '登录账号', 'admin', 'system.login.account', 'input', NULL, 1, '登录账号', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (6, '登录密码', 'admin', 'system.login.password', 'password', NULL, 1, '登录密码', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (7, '开启降级下载', 'false', 'system.download.degrade', 'boolean', NULL, 0, '暂时未开发功能后续准备', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (8, '排除的歌单名称', '示例1|示例2', 'system.sync.playlist.exclude', 'input', NULL, 1, '排除的歌单名称多个|（管道符）分割', 0, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (9, '排除的专辑名称', '示例1|示例2', 'system.sync.album.exclude', 'input', NULL, 1, '排除的专辑名称多个|（管道符）分割', 0, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (10, '排除的歌手名称', '示例1|示例2', 'system.sync.artists.exclude', 'input', NULL, 1, '排除的歌手名称多个|（管道符）分割', 0, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (11, '是否显示播放连接', 'true', 'system.show.play.url', 'boolean', NULL, 1, '是否显示播放连接（下载链接）', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (12, 'QQ音乐插件启用', 'false', 'plug.qqvip.open', 'boolean', NULL, 1, '开启QQvip插件类型', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (13, 'QQ音乐自动刷新登录', 'false', 'plug.qqvip.auto.refresh.login', 'boolean', NULL, 1, '是否开启QQ音乐自动刷新登录信息', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (14, 'QQ音乐自动下载我喜欢的歌曲', 'false', 'plug.qqvip.sync.my.like.music', 'boolean', NULL, 1, 'QQ音乐自动下载我喜欢音乐内容', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (15, 'QQ音乐自动下载我收藏专辑', 'false', 'plug.qqvip.sync.my.like.album', 'boolean', NULL, 1, '忽略专辑可在排除的专辑名称设置', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (16, 'QQ音乐自动下载我关注的歌手', 'false', 'plug.qqvip.sync.my.like.artists', 'boolean', NULL, 1, '忽略歌手可在排除的歌手名称设置', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (17, 'QQ音乐自动下载我收藏歌单内容', 'false', 'plug.qqvip.sync.my.like.playlist', 'boolean', NULL, 1, '忽略歌单名称可以通过排除歌单名称设置', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (18, 'QQ音乐cookie', '', 'plug.qqvip.cookie', 'input', NULL, 0, '看看就行尽量别改', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (19, 'QQ音乐qrcode', '', 'plug.qqvip.qrcode', 'input', NULL, 0, '看看就行尽量别改', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (20, '开启网易云音乐插件', 'true', 'plug.netease.open', 'boolean', NULL, 1, '开启网易云音乐插件', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (21, '网易云音乐API地址', 'https://zm.wwoyun.cn;https://ncm.landdy.cn', 'plug.netease.baseurl', 'input', NULL, 1, '类似http://xxx.xxxx.com(后边不要带/)多个;分割', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (22, '网易云音乐AP匿名登录地址', '/register/anonimous', 'plug.netease.cookieurl', 'input', NULL, 1, '一般是API地址加上/register/anonimous', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (23, '网易云音乐用户信息', '', 'plug.netease.cookie', 'input', NULL, 1, '网易云音乐的cookie', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (24, '是否使用扩展下载', 'true', 'plug.netease.extend.download', 'boolean', NULL, 1, '扩展可下载VIP（如果自己的cookie是VIP也可以设置为否）', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (25, '网易云音乐扩展下载地址', 'https://music-api.gdstudio.xyz/api.php', 'plug.netease.extend.download.url', 'input', NULL, 1, '网易云音乐扩展下载地址', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (26, '开启酷狗插件', 'false', 'plug.kg.open', 'boolean', NULL, 1, '开启酷狗插件', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (27, '酷狗概念API地址', '', 'plug.kg.baseurl', 'input', NULL, 1, 'GIT:https://github.com/MakcRe/KuGouMusicApi 部署此项目(1.4.0版本)', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (28, '酷狗用户信息', '', 'plug.kg.user.info', 'input', NULL, 1, '酷狗用户信息', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (29, '酷狗二维码信息', '', 'plug.kg.qrcode.code', 'input', NULL, 0, '酷狗二维码信息', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (30, '酷狗微信二维码信息', '', 'plug.kg.qrcode.wx.code', 'input', NULL, 0, '酷狗微信二维码信息', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (31, '酷狗开启自动下载我收藏的歌单', 'true', 'plug.kg.sync.my.collect.playlist', 'boolean', NULL, 1, '忽略歌单名称可以通过排除歌单名称设置', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (32, '开启自动签到', 'true', 'plug.kg.sign.open', 'boolean', NULL, 1, '定时自动获得VIP', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (33, '酷狗最后一次自动签到时间', '', 'plug.kg.sign.last.time', 'input', NULL, 1, '酷狗最后一次自动签到时间', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (34, '开启酷我插件', 'true', 'plug.kw.open', 'boolean', NULL, 1, '开启酷我插件', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (35, '酷狗概念签到到期信息', '', 'plug.kg.sign.begin-end.time', 'input', NULL, 1, '酷狗概念最后一次签到指示的开始与结束时间', 0, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (36, '同步补充同步歌单信息', 'false', 'system.sync.update.playlist.info', 'boolean', NULL, 1, '2.0版本导入的歌单信息缺失歌手信息和专辑信息如果碰到下载相同的歌曲时候补充信息，如果是没有历史歌单导入或者3.0版本开始使用则不用开启', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (37, '下载歌曲文件名模板', '${artists}/${album}/${musicName} - ${artists}', 'system.download.file.template', 'input', NULL, 1, E'描述：目前支持\r\n1.${musicName}---歌曲名称\r\n2.${artists}---歌手名称（全部歌手）\r\n3.${album}---专辑名称\r\n4.${albumId}---专辑ID\r\n5.${artistsId}---歌手id\r\n6.${artist}------主要歌手（只有1个）\r\n7.${albumTime}---------专辑时间（2020-05-21）\r\n8.${albumYear}---------专辑年份（2020）\r\n9.${albumMonth}---------专辑月份（05）\r\n10.${albumDay}---------专辑日（21）\r\n默认为${artists}/${album}/${musicName} - ${artists}---/歌手/专辑/歌曲名称 - 歌手名称 \r\n${album}/${musicName} - ${artists}  ------------专辑/歌曲名称 - 歌手名称 \r\n示例：\r\n-----------------------------\r\n模板：${artists}/${album}/${musicName} - ${artists}\r\n歌手目录: 周杰伦\r\n专辑目录: 周杰伦\\范特西\r\n歌曲目录: 周杰伦\\范特西\\双截棍 - 周杰伦.flac\r\n-----------------------------\r\n模板：${album}/${musicName} - ${artists}\r\n歌手目录: null\r\n专辑目录: 范特西\r\n歌曲目录: 范特西\\双截棍 - 周杰伦.flac\r\n-----------------------------\r\n模板：/自定义文件1/${artists}/自定义文件夹2/${album}/${musicName}/自定义文件3/${musicName} - ${artists}\r\n歌手目录: 自定义文件1\\周杰伦\r\n专辑目录: 自定义文件1\\周杰伦\\自定义文件夹2\\范特西\r\n歌曲目录: 自定义文件1\\周杰伦\\自定义文件夹2\\范特西\\双截棍\\自定义文件3\\双截棍 - 周杰伦.flac\r\n高级支持SpEL表达式具体百度）', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (38, '开启特殊字符移除', 'true', 'system.start.file.and.folder.special.symbol.remove', 'boolean', NULL, 1, '开启后配置需要移除的特护符号(建议开启部分歌曲歌手专辑名称有系统无法识别的字符，关闭有可能部分歌曲无法下载！)', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (39, '特殊符号配置', E'*?:|\"\\/<>', 'system.start.file.and.folder.special.symbol.remove.symbol', 'input', NULL, 1, '开启特殊字符移除后生效', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (42, '下载文件音频格式', 'auto', 'system.download.file.audio.format', 'select', '[{"label":"自动","value":"auto"},{"label":"只下载flac","value":"flac"},{"label":"只下载mp3","value":"mp3"},{"label":"只下载ape","value":"ape"},{"label":"只下载wav","value":"wav"},{"label":"只下载m4a","value":"m4a"},{"label":"只下载ogg","value":"ogg"},{"label":"只下载aac","value":"aac"}]', 1, E'自动则是默认下载最高音质，有选择的格式则下载指定的格式\r\n支持情况(flac和mp3通用):\r\n-kw mp3,ape,flac-mg mp3-qqvip m4a,mp3,ape,flac-kg mp3-apple(目前未上线) aac,ogg,m4a,mp3,wav,flac,alac-tidal mp3,flac-qobuz(国外综合，目前未上线) mp3,flac', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (45, '阿里云同步扩展', NULL, 'expand.aliyun.open', 'boolean', NULL, 1, '打开则开启自动同步歌曲到阿里云音乐，同步模式在同步模式中选择', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (46, '阿里云盘Appid', NULL, 'expand.aliyun.appid', 'input', NULL, 1, '阿里云盘开发者的appid', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (47, '阿里云盘secret', NULL, 'expand.aliyun.appsecret', 'input', NULL, 1, '目前用的无后台模式暂时用不到', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (48, '阿里云盘刷新token', NULL, 'expand.aliyun.refresh_token', 'input', NULL, 1, '目前用的无后台模式暂时用不到', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (50, '阿里云盘的backup_drive_id', NULL, 'expand.aliyun.backup.drive_id', 'input', NULL, 1, '资源文件夹的ID', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (51, '阿里云盘的资源drive_id', NULL, 'expand.aliyun.resource.drive_id', 'input', NULL, 1, '资源文件夹的ID', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (52, '阿里云盘的请求token', NULL, 'expand.aliyun.access_token', 'input', NULL, 1, 'access_token 用户信息token', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (53, '阿里云盘的请求token有效时长', NULL, 'expand.aliyun.code_verifier', 'input', NULL, 1, '单位是秒（无后端服务情况下只支持30天有效）', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (54, '阿里云盘的授权码', NULL, 'expand.aliyun.code', 'input', NULL, 1, '授权码 获取到请求token后会很快失效过度使用', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (55, '阿里云盘用户名称', NULL, 'expand.aliyun.user.name', 'input', NULL, 1, '阿里云盘用户名称', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (56, '阿里云盘头像url', NULL, 'expand.aliyun.avatar', 'input', NULL, 1, '阿里云盘头像url', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (57, '阿里云用户id', NULL, 'expand.aliyun.user.id', 'input', NULL, 1, '阿里云用户id', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (58, '阿里云盘的账号名称', NULL, 'expand.aliyun.user.info.name', 'input', NULL, 1, '阿里云盘的账号名称', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (59, '阿里云盘昵称', NULL, 'expand.aliyun.nick.name', 'input', NULL, 1, '阿里云盘的昵称', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (60, '存储到阿里云盘的目录位置', NULL, 'expand.aliyun.folder.path', 'input', NULL, 1, '存储到阿里云盘的目录位置（默认在 备份文件/SqMusic文件夹）', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (61, '同步模式', NULL, 'expand.aliyun.sync.mode', 'select', '[{"label":"定时同步和下载后自动同步","value":"all"},{"label":"定时同步到云盘","value":"scheduled"},{"label":"歌曲下载完成后同步","value":"download"}]', 1, '1.支持定时扫描下载文件夹内的全部歌曲同步上传到阿里云/n2.支持歌曲下载完成后同步到阿里云盘内', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (62, '阿里云盘access_token到期时间', NULL, 'expand.aliyun.access_token.expire.time', 'input', NULL, 1, '阿里云盘access_token到期时间', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (63, '显示阿里云盘扩展', 'false', 'plug.aliyun.show', 'boolean', NULL, 1, '控制显示和隐藏阿里云扩展', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (64, 'QQ音乐每日最大限额', '2000', 'plug.qqvip.download.daily.limit', 'number', NULL, 1, '每天下载超过这个数值就会暂停等待第二天下载(获取下载链接的次数防止被封号)', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (65, 'QQ音乐今日下载', '0', 'plug.qqvip.download.today', 'number', NULL, 1, '今日下载数量10点整刷新', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (66, '开启咪咕音乐插件', 'true', 'plug.mg.open', 'boolean', NULL, 1, '开启咪咕音乐插件', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (67, '开启Tidal音乐插件', 'true', 'plug.tidal.open', 'boolean', NULL, 1, '开启Tidal音乐插件', 1, 1) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (68, 'Tidal访问token', NULL, 'plug.tidal.token', 'input', 'Tidal访问token', 1, '会自动刷新不用修改', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (69, '流量监控显示', 'true', 'system.show.traffic.monitoring', 'boolean', '流量监控显示', 1, '控制左上角实时流量是否展示', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (70, '是否启用下载失败歌曲使用其他插件代替下载', 'true', 'system.download.failed.use.other.plugin', 'boolean', '', 1, '如果当前歌曲下载失败，开启后则会根据歌曲名称，专辑，歌手，码率等信息从其他插件中获取下载连接', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (71, '下载失败时使用其他插件的顺序', 'kw,netease,qqvip,mg,kg', 'system.download.failed.use.other.plugin.order', 'input', '', 1, '下载失败后使用其他插件重新下载的顺序，多个插件用逗号分隔', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (72, '下载失败时使用其他插件搜索歌曲的匹配模式', 'name_artist_alubm_like', 'system.download.failed.use.other.plugin.match.mode', 'select', '[{"label":"严格匹配（名称+歌手+专辑完全一致）","value":"strict"},{"label":"名称+歌手+专辑包含匹配","value":"name_artist_alubm_like"},{"label":"名称+歌手包含匹配（推荐）","value":"name_artist_fuzzy"},{"label":"仅名称包含匹配（最宽松）","value":"name_fuzzy"}]', 1, 'strict=名称+歌手+专辑完全一致；name_artist_alubm_like=名称+歌手+专辑包含即可；name_artist_fuzzy=名称+歌手包含匹配（推荐）；name_fuzzy=仅名称包含匹配（最宽松）', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (73, '下载超时歌曲重试间隔时长（分）', '720', 'system.download.timeout.retry.interval', 'number', NULL, 1, '默认12小时后重试--有些接口调用太多会超时需要间隔时间比较久', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (74, '插件下载超时重试次数', '3', 'system.download.timeout.retry.num', 'number', NULL, 1, '插件下载超时重试次数', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (75, '歌曲tag标签数据来源', 'auto', 'plug.qqvip.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (76, '歌曲tag标签数据来源', 'auto', 'plug.mg.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (77, '歌曲tag标签数据来源', 'auto', 'plug.kg.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (78, '歌曲tag标签数据来源', 'qq', 'plug.kw.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0) ON CONFLICT (config_id) DO NOTHING;
INSERT INTO sq_config (config_id, config_name, config_value, config_key, config_type, config_options, config_show, config_remark, config_null_check, config_disabled) VALUES (79, '歌曲tag标签数据来源', 'auto', 'plug.netease.tag.source', 'select', '[{"label":"当前插件","value":"auto"},{"label":"qq音乐（推荐）","value":"qq"},{"label":"网易云音乐","value":"netease"},{"label":"咪咕","value":"mg"},{"label":"酷我（不推荐 没有CD标识 全部歌按顺序排序）","value":"kw"},{"label":"酷狗（需要开启插件并且登录）","value":"kg"}]', 1, '歌曲tag标签数据来源默认跟随自身插件', 1, 0) ON CONFLICT (config_id) DO NOTHING;

-- ==================== 重置序列（固定 config_id 插入后序列不会自动推进） ====================

SELECT setval(pg_get_serial_sequence('sq_config', 'config_id'), COALESCE((SELECT MAX(config_id) FROM sq_config), 0) + 1, false);
SELECT setval(pg_get_serial_sequence('download_info', 'id'), COALESCE((SELECT MAX(id) FROM download_info), 0) + 1, false);
SELECT setval(pg_get_serial_sequence('sq_sync', 'id'), COALESCE((SELECT MAX(id) FROM sq_sync), 0) + 1, false);
SELECT setval(pg_get_serial_sequence('sq_monitor', 'id'), COALESCE((SELECT MAX(id) FROM sq_monitor), 0) + 1, false);
SELECT setval(pg_get_serial_sequence('sq_ali_sync', 'id'), COALESCE((SELECT MAX(id) FROM sq_ali_sync), 0) + 1, false);
