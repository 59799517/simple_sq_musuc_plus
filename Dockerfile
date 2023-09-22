FROM alpine:3.17.2
CMD ["/bin/sh"]
EXPOSE 8083
MAINTAINER 59799517@qq.com
# 创建工作目录
WORKDIR /root
# 修改软件包源地址(此处使用 清华大学的源地址)
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories
# 更新软件包
RUN apk update upgrade
RUN apk add --no-cache ca-certificates tzdata tree curl tini
# 安装 glibc 库,主要为了解决中文乱码, 但是有部分java工程可能会依赖. 比如: aws kinesis 等
COPY locale.md locale.md
COPY glibc-2.35-r0.apk glibc-2.35-r0.apk
COPY glibc-bin-2.35-r0.apk glibc-bin-2.35-r0.apk
COPY glibc-i18n-2.35-r0.apk glibc-i18n-2.35-r0.apk
COPY sgerrand.rsa.pub /etc/apk/keys/sgerrand.rsa.pub
RUN apk add glibc-2.35-r0.apk glibc-bin-2.35-r0.apk glibc-i18n-2.35-r0.apk
RUN cat locale.md | xargs -i /usr/glibc-compat/bin/localedef -i {} -f UTF-8 {}.UTF-8 && \
rm -rf *.apk && \
rm -rf /var/cache/apk/* && \
rm -rf locale.md
# tzdata 是可以配置时区,这里默认使用上海时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
RUN { echo '#!/bin/sh'; echo 'set -e'; echo; echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; } > /usr/local/bin/docker-java-home
RUN chmod +x /usr/local/bin/docker-java-home
# 支持使用中文
ENV LANG=zh_CN.UTF-8
ENV LANGUAGE=zh_CN.UTF-8


# 以下为安装jdk 17 的命令 , 目前alpine:3.15 里面软件包最新版本 jdk版本 11.0.15
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk
ENV PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/lib/jvm/java-11-openjdk/jre/bin:/usr/lib/java-11-openjdk/bin
RUN apk add --no-cache openjdk17 && [ "$JAVA_HOME" = "$(docker-java-home)" ]


# 以下为安装jdk 1.8 的命令 , 目前alpine:3.15 里面软件包最新版本 jdk版本 1.8.0_322
# ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
# ENV PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin
# RUN apk add --no-cache openjdk8 && [ "$JAVA_HOME" = "$(docker-java-home)" ]



ARG JAR_FILE
VOLUME ["/music"]
VOLUME ["/config"]
ADD ./simple-MusicServer2.0.jar  /app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]
