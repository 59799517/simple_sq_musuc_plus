FROM openjdk:17.0.2-jdk-oracle
EXPOSE 8083
ARG JAR_FILE
USER root
ADD ./simple-MusicServer-0.0.1-SNAPSHOT.jar  /sqmusic.jar
ADD ./entrypoint.sh  /entrypoint/entrypoint.sh
CMD ["chmod u+x /entrypoint/entrypoint.sh"]
ENTRYPOINT ["/entrypoint/entrypoint.sh"]