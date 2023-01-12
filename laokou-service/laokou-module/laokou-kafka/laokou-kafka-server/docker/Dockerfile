# 基础镜像
FROM docker.io/sunrdocker/jdk17-jre-font-openssl-alpine:latest
# 信息
LABEL maintainer="koushenhai"
LABEL description="laokou-kafka-server"
# 暴露端口
EXPOSE 10002
# 复制文件
COPY laokou-kafka-server.jar app.jar
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
# 执行命令
ENTRYPOINT java -Xms512m -Xmx512m -jar app.jar --spring.profiles.active=prod > app.log