# 第一阶段：提取分层 JAR（直接使用预构建 JAR，避免在 Docker 内重复编译）
FROM amazoncorretto:21-alpine AS extractor
LABEL maintainer="SQ"
WORKDIR /extractor

# 声明构建参数（默认值兼容本地开发：先 mvn package 再 docker build）
ARG JAR_FILE=target/simple_sq_music_plus.jar
# 复制预构建 JAR（CI 中由 build job 产出并通过 artifact 传入）
COPY ${JAR_FILE} app.jar

# 使用 Spring Boot 的分层工具提取 JAR（自定义 layers.xml 将重型依赖隔离到 heavy-native 层）
# 提取后把解出的文件时间戳统一归一：即使每次重新 mvn package 时间不同，只要依赖版本不变，
# heavy-native/dependencies 层的内容(含 mtime)就逐字节一致 → 各版本镜像 layer digest 不变，
# 用户更新时 Docker 对这些层显示 Already exists，只增量下载小层，不会每次重下 ~1GB 大包
RUN java -Djarmode=layertools -jar app.jar extract --destination /extractor/layers && \
    find /extractor/layers -exec touch -t 200001010000 {} +

# 第二阶段：运行环境
FROM amazoncorretto:21-alpine

# 设置工作目录
WORKDIR /app

# 按稳定性从高到低复制（最稳定的放最前面，优化 Docker 缓存）
# heavy-native 层：jave/javacv/nashorn 重型依赖，版本不变则永远缓存
COPY --from=extractor /extractor/layers/heavy-native/ ./
# dependencies 层：其余 Maven 依赖
COPY --from=extractor /extractor/layers/dependencies/ ./
# spring-boot-loader 层
COPY --from=extractor /extractor/layers/spring-boot-loader/ ./
# application 层：业务代码（最常变）
COPY --from=extractor /extractor/layers/application/ ./

# 显示架构信息（便于调试）
RUN echo "Running on architecture: $(uname -m)" && \
    echo "Java version:" && java -version

# 设置 JVM 参数优化
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 暴露端口
EXPOSE 8099

# 挂载音乐目录
VOLUME ["/music"]

# 启动应用
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
