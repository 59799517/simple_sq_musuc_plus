#!/bin/bash


echo "java-端口：$0";
echo "web-端口：$1";

# 启动nginx服务
nginx -c /usr/local/nginx/conf/nginx.conf

#启动后端jar包,日志打印不额外存储
nohup java -Dserver.port=$0 -jar /app/app.jar >/dev/null 2>& 1 &

#使这个脚本一直处于运行状态,如果不这样,当这个脚本命令执行结束后,docker容器会立即停止运行,所以这里需要让这个脚本一直运行,使docker容器一直处于运行状态
while [[ true ]];do
  sleep 1
done
