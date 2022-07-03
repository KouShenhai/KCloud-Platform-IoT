# 基础镜像
FROM openjdk:8-jre-alpine
# 信息
LABEL maintainer="koushenhai"
LABEL description="laokou-register"
# 开放端口
EXPOSE 1111
# 挂载
VOLUME /data
# 复制文件
COPY register.jar /opt/register.jar
# 执行命令
ENTRYPOINT java -Xms256m -Xmx512m -jar /opt/register.jar > /opt/register.log