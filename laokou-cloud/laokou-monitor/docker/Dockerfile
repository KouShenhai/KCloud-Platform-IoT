# 基础镜像
FROM docker.io/sunrdocker/jdk17-jre-font-openssl-alpine:latest
# 信息
LABEL maintainer="koushenhai"
LABEL description="laokou-monitor"
# 开放端口
EXPOSE 5000
# 挂载
VOLUME /data
# 复制文件
COPY laokou-monitor.jar app.jar
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
# 执行命令
ENTRYPOINT java -Xms512m -Xmx512m -jar app.jar > app.log