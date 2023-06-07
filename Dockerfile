FROM tomcat:8.5.87-jre17-temurin-focal
#接口地址
EXPOSE 8080
MAINTAINER 59799517@qq.com

COPY ./web /usr/local/tomcat/webapps/ROOT
COPY ./sw.war /usr/local/tomcat/webapps/sw.war
#缓存地址
VOLUME ["/cache/sqmusic/config/cache"]
#日志地址
VOLUME ["/cache/sqmusic/config/logs"]

ENTRYPOINT ["catalina.sh", "run"]
