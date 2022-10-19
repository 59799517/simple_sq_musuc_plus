FROM openjdk:17.0.2-jdk-oracle
EXPOSE 8083
ARG JAR_FILE
VOLUME ["/music"]
VOLUME ["/config"]
ADD ./simple-MusicServer-0.0.2-Beta.jar  /app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]