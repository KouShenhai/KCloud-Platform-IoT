# 基础镜像
FROM openjdk:8-jre-alpine
# 信息
LABEL maintainer="koushenhai"
LABEL description="laokou-gateway"
# 暴露端口
EXPOSE 5555
# 复制文件
COPY gateway.jar /opt/gateway.jar
# 执行命令
ENTRYPOINT java -Xms256m -Xmx512m -jar /opt/gateway.jar > /opt/gateway.log
