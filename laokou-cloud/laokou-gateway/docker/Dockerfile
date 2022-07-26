# 基础镜像
FROM openjdk:8-jre-alpine
# 信息
LABEL maintainer="koushenhai"
LABEL description="laokou-gateway"
# 暴露端口
EXPOSE 5555
# 复制文件
COPY gateway.jar /opt/gateway.jar
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime && echo Asia/Shanghai > /etc/timezone
# 执行命令
ENTRYPOINT java -Xms256m -Xmx512m -Djasypt.encryptor.password=koushenhai5201314wumeihua -Denv=DEV -Dapollo.configService=http://124.222.196.51:8080 /opt/gateway.jar --spring.profiles.active=prod > /opt/gateway.log
