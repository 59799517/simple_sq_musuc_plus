docker build -t sqmusicplusserver .
docker rmi sqmusicplusserver


docker run --name="sqmusicplusserver" -e UID=1000 -e GID=100  -p 8022:8083 -v /mnt/user/media/newmusic:/music  -v  /mnt/user/appdata/sqmusic:/sqmusic sqmusicplusserver


### 单曲下载
### 监听10分钟是否卡死卡死重启
### 歌单错误重试
### 有声下载
### 有声整理（文件名称与文件夹整理）





