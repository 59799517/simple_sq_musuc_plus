
### 出现问题先升级到最新版本
### simple_sq_music_plus
是下载音乐工具，可以当普通的音乐下载工具使用，支持，flac，ape，mp3等格式的下载（根据码率不同）， 下载的歌曲目录结构支持emby 与 subsonic（后续开放） 类的服务，下载文件支持文件标签识别，歌词下载。
```js
\Music下载根路径
       \歌手名称
               \专辑名称
                       1- 歌曲1.flac
                       2- 歌曲2.flac

```
默认支持群辉等第三方音乐服务标识：
emby,jellyfin识别请参考如下配置 https://support.emby.media/support/solutions/articles/44001159113-music-naming

- 默认账号：admin
- 默认密码：admin  （登录后设置自行修改）




效果截图
![wechat_2025-09-28_160749_034.png](img/wechat_2025-09-28_160749_034.png)
![wechat_2025-09-28_160832_175.png](img/wechat_2025-09-28_160832_175.png)
![wechat_2025-09-28_160855_350.png](img/wechat_2025-09-28_160855_350.png)
![wechat_2025-09-28_160921_626.png](img/wechat_2025-09-28_160921_626.png)
![wechat_2025-09-28_160933_756.png](img/wechat_2025-09-28_160933_756.png)
![wechat_2025-09-28_161030_965.png](img/wechat_2025-09-28_161030_965.png)
![wechat_2025-09-28_161133_048.png](img/wechat_2025-09-28_161133_048.png)
![wechat_2025-09-28_161212_579.png](img/wechat_2025-09-28_161212_579.png)
![wechat_2025-09-28_161306_157.png](img/wechat_2025-09-28_161306_157.png)
![wechat_2025-09-28_161505_837.png](img/wechat_2025-09-28_161505_837.png)
![wechat_2025-09-28_161552_590.png](img/wechat_2025-09-28_161552_590.png)


#### 接口文档：
https://59799517.github.io/simple_sq_music_plus/#/README
#### MCP
```agsl
- 访问地址：
- http(s)://IP:端口/mcp
- 请求头：
- sqmusic=Token(设置下方的token值)
```




### 运行项目
#### 1. docker-compose（推荐--mysql启动慢导致报错可以多运行几次）
    运行docker-compose文件即可（本地编译使用docker-compose-local）
#### 2.docker启动请参考docker-compose配置手动启动
1. 启动mysql
```dockerfile
# 拉取 MySQL 5.7 镜像
docker pull mysql:5.7

# 创建自定义网络
docker network create sq-app-network

# 运行 MySQL 容器
docker run -d \
  --name sqmusic_mysql \
  --restart=always \
  -e MYSQL_ROOT_PASSWORD=sqmusicv3password \
  -e MYSQL_DATABASE=sqmusicv3 \
  -v ./mysql_data:/var/lib/mysql \
  -p 3306:3306 \
  --network simple_sq_music_plus_sq-app-network \
  mysql:5.8
```
2. 启动后端服务
```dockerfile
# 拉取后端服务镜像（使用最新版本号）
docker pull registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v3.0.8

# 运行后端容器
docker run -d \
  --name sqmusic_main \
  --restart=always \
  -e DB_IP=mysql \
  -e DB_PORT=3306 \
  -e DB_NAME=sqmusicv3 \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=sqmusicv3password \
  -v ./music:/music \
  --network simple_sq_music_plus_sq-app-network \
  registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:latest
```
3. 启动前段服务
```dockerfile
# 拉取前端服务镜像（使用最新版本号）
docker pull registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:v3.0.5

# 运行前端容器
docker run -d \
  --name sqmusic_web \
  --restart=always \
  -p 8996:80 \
  --network simple_sq_music_plus_sq-app-network \
  registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:latest

```

Todo：
1. ~~页面增加播放页面播放删除~~
2. 支持苹果音乐（只能做到mp3 暂时不弄了  没无损无意义）
3. ~~增加网易云监听歌单~~
4. ~~增加指定格式下载~~
5. ~~增加当前下载速度展示~~
6. 增加下载暂停功能
7. ~~手机模式下专辑和歌手不展示问题~~
8. ~~增加阿里云盘同步~~
9. ~~增加QQ音乐超限制暂停~~
10. ~~增加咪咕音乐~~ 
11. ~~增加api文档~~
12. ~~下载失败使用其他插件进行下载~~
13. ~~综合查询下载不根据单一插件下载查询~~
14. ~~增加下载超时重试功能（设置增加重试间隔时间）~~
15. ~~根据条件自定义处理下载功能（删除重试等）~~
16. ~~增加内置网易云功能~~

下载模板支持（3.1.15版本后取消el表达式支持-有安全风险需要提出issues）
描述：目前支持
1. ${musicName}---歌曲名称
2. ${artists}---歌手名称（全部歌手）
3. ${album}---专辑名称
4. ${albumId}---专辑ID
5. ${artistsId}---歌手id
6. ${artist}------主要歌手（只有1个）
7. ${albumTime}---------专辑时间（2020-05-21）
8. ${albumYear}---------专辑年份（2020）
9. ${albumMonth}---------专辑月份（05）
10. ${albumDay}---------专辑日（21）
```
默认为${artists}/${album}/${musicName} - ${artists}---/歌手/专辑/歌曲名称 - 歌手名称
${album}/${musicName} - ${artists}  ------------专辑/歌曲名称 - 歌手名称```
示例：
-----------------------------
模板：${artists}/${album}/${musicName} - ${artists}
歌手目录: 周杰伦
专辑目录: 周杰伦\范特西
歌曲目录: 周杰伦\范特西\双截棍 - 周杰伦.flac
-----------------------------
模板：${album}/${musicName} - ${artists}
歌手目录: null
专辑目录: 范特西
歌曲目录: 范特西\双截棍 - 周杰伦.flac
-----------------------------
模板：/自定义文件1/${artists}/自定义文件夹2/${album}/${musicName}/自定义文件3/${musicName} - ${artists}
歌手目录: 自定义文件1\周杰伦
专辑目录: 自定义文件1\周杰伦\自定义文件夹2\范特西
歌曲目录: 自定义文件1\周杰伦\自定义文件夹2\范特西\双截棍\自定义文件3\双截棍 - 周杰伦.flac
```

[//]: # (高级支持SpEL表达式具体百度）)



感谢：
1. https://github.com/MakcRe/KuGouMusicApi  酷狗API
2. https://music.gdstudio.org  网易API下载支持



### 2.x迁移3.x版本
1. 导出已经同步过的歌单、专辑、歌手信息（文件是.json）
   ![img.png](img/img.png)
2. 3.0版本导入已经同步信息(时间较长耐心等待)
   ![wechat_2025-09-28_163226_700.png](img/wechat_2025-09-28_163226_700.png)


   # 免责声明

本音乐下载工具**仅用于个人技术学习、代码研究与技术测试目的**，仅供学习交流使用。

1. 工具使用者知悉：音乐作品受著作权法保护，未经版权方授权，不得私自下载、复制、传播、商用相关音频文件。
2. 若您使用本工具获取音频资源，请**仅限 24 小时内完成学习测试，并在 24 小时内彻底删除所下载的全部文件**。若超过时限继续留存，相关责任由使用者自行承担。
3. 使用者承诺，不会将本工具及下载得到的音乐资源用于商业用途、公开分享、二次分发等任何侵犯知识产权的行为。
4. 本工具作者不对使用者的任何使用行为承担法律责任，如使用者违反版权相关法律法规产生纠纷、赔偿、行政处罚等一切后果，均由使用者本人独立承担。
5. 如果您不同意本免责声明，请立即停止使用本工具。

