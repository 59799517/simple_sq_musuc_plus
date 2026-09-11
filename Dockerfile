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

# 【重要】不要在这里写 WORKDIR /app！
# WORKDIR 创建 /app 时会把"构建时间"写进该层目录项的 mtime（实测：3.1.27 是 09-07 17:02，
# 3.1.28 是 09-11 09:14），于是这一层 digest 每次构建都不同 → 其后所有层的 chainID（累积链 ID）
# 全部变化 → 即使 heavy-native 大层内容逐字节一致，Docker 也认为是"新层"，用户被迫重下 ~1.1GB。
# 改为用 COPY 的绝对路径创建 /app：目录项 mtime 取源目录（已被 touch 归一化为 2000-01-01），恒定不变。

# 按稳定性从高到低复制（最稳定的放最前面，优化 Docker 缓存）
# heavy-native 层：jave/javacv/nashorn 重型依赖，版本不变则永远缓存
COPY --from=extractor /extractor/layers/heavy-native/ /app/
# dependencies 层：其余 Maven 依赖
COPY --from=extractor /extractor/layers/dependencies/ /app/
# spring-boot-loader 层
COPY --from=extractor /extractor/layers/spring-boot-loader/ /app/
# application 层：业务代码（最常变）
COPY --from=extractor /extractor/layers/application/ /app/

# 设置工作目录：此时 /app 已存在，只写镜像 config，不再产生带构建时间的层
WORKDIR /app

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
