FROM openjdk:17.0.2-jdk-oracle
EXPOSE 8083
ARG JAR_FILE
ADD ./simple-MusicServer-0.0.1-SNAPSHOT.jar  /app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]