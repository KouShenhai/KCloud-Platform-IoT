# 基础镜像
FROM docker.io/sunrdocker/jdk17-jre-font-openssl-alpine:latest
# 信息
LABEL maintainer="koushenhai"
LABEL description="laokou-elasticsearch-server"
# 暴露端口
EXPOSE 10003
# 复制文件
COPY laokou-elasticsearch-server.jar app.jar
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
# 执行命令
ENTRYPOINT java -Xms512m -Xmx512m --add-opens=java.base/java.lang=ALL-UNNAMED \
-Denv=DEV \
-Dapollo.configService=http://124.222.196.51:8080 \
-jar app.jar > app.log
