FROM maven:3.9.4-eclipse-temurin-17-alpine AS builder
MAINTAINER SQ

WORKDIR /build/

COPY pom.xml /build/
COPY src /build/src/

RUN mvn clean package

From eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder  /build/target/MusicServer2.0.jar /app/app.jar

EXPOSE 8099
VOLUME ["/music"]
VOLUME ["/config"]

CMD ["java", "-jar", "app.jar"]

