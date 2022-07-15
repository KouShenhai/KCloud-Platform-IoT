# 基础镜像
FROM openjdk:8-jre-alpine
# 信息
LABEL maintainer="koushenhai"
LABEL description="laokou-monitor"
# 开放端口
EXPOSE 5000
# 挂载
VOLUME /data
# 复制文件
COPY monitor.jar /opt/monitor.jar
# 执行命令
ENTRYPOINT java -Xms256m -Xmx512m -jar /opt/monitor.jar --spring.profiles.active=prod > /opt/monitor.log