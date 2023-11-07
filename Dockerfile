#编译jar包
FROM maven:3.9.4-eclipse-temurin-17-alpine AS builder

MAINTAINER SQ

WORKDIR /build/
# 时区与字符设置UTF-8并配置环境
ENV TZ=Asia/Shanghai
ENV LANG=C.UTF-8
ENV apiPort="8099"
ENV webPort="80"
ENV TimeZone=Asia/Shanghai

#复制源码信息
COPY pom.xml /build/
COPY src /build/src/
COPY src/main/resources/sqlite/sqmusic.db /cache/sqmusic.db

#打包
RUN mvn clean package


#运行镜像
FROM centos:7


RUN yum update \
    && yum install -y java-17-openjdk nginx

COPY --from=builder  /build/target/MusicServer2.0.jar /app/app.jar

RUN echo "worker_processes  1;events {    worker_connections  1024;}http {    include       mime.types;    default_type  application/octet-stream;    sendfile        on;    keepalive_timeout  65;    server {        listen       ${webPort};        server_name  localhost;        location / {            root   html;            index  index.html index.htm;        }        location /sqmusic-api/ {            proxy_pass http://localhost:${apiPort}/;            proxy_http_version 1.1;        }        error_page   500 502 503 504  /50x.html;        location = /50x.html {            root   html;        }    }}" > /usr/local/nginx/conf/nginx.conf


#将启动脚本拷贝到容器里面的/usr/local/project下面
COPY run.sh /usr/local/project
#给run.sh可执行权限
RUN chmod 777 /usr/local/project/docker-run.sh

#对外暴露80,8880端口,暴不暴露端口没有什么影响,重要的是要在启动的时候使用-p映射宿主机端口:容器端口,暴露端口你也得使用-p映射端口
EXPOSE ${apiPort} ${webPort}

#nginx -c -t /software/nginx/nginx.conf

#通过脚本同时启动后端jar包和nginx
CMD ["sh","/usr/local/project/docker-run.sh","${apiPort}","${webPort}"]
