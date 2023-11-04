FROM maven:3.9.4-eclipse-temurin-17-alpine AS builder
FROM nginx as nginx
MAINTAINER SQ



WORKDIR /build/

ENV apiPort="8099"
ENV webPort="80"
ENV TimeZone=Asia/Shanghai

COPY pom.xml /build/
COPY src /build/src/
COPY src/main/resources/sqlite/sqmusic.db /cache/sqmusic.db


RUN mvn clean package

From eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN mkdr -p /usr/share/nginx/html/
RUN mkdr -p /etc/nginx/conf.d/
RUN echo "worker_processes  1;events {    worker_connections  1024;}http {    include       mime.types;    default_type  application/octet-stream;    sendfile        on;    keepalive_timeout  65;    server {        listen       $webPort;        server_name  localhost;        location / {            root   html;            index  index.html index.htm;        }        location /sqmusic-api/ {            proxy_pass http://localhost:$apiPort/;            proxy_http_version 1.1;        }        error_page   500 502 503 504  /50x.html;        location = /50x.html {            root   html;        }    }}" > /etc/nginx/conf.d/default.conf


COPY --from=builder  /build/target/MusicServer2.0.jar /app/app.jar
COPY --from=nginx  src/main/resources/static/ /usr/share/nginx/html/


EXPOSE 8099
EXPOSE 80

VOLUME ["/music"]

VOLUME ["/cache"]

CMD ["nginx -g daemon off;","java -jar app.jar --server.port=$apiPort"]

