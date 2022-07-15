# 基础镜像
FROM jdk:1.8
# 信息
LABEL maintainer="koushenhai"
LABEL description="laokou-apollo"
# 开放端口
EXPOSE 8080 8090 8070
# 挂载
VOLUME /data
# 复制文件
ADD apollo.zip /opt/apollo.zip
# 运行
RUN cd /opt \
    && mkdir -p /opt/apollo \
    && yum install -y unzip zip \
    && unzip apollo.zip -d /opt/apollo \
    && chmod 777 /opt/apollo/demo.sh
# 执行命令
ENTRYPOINT cd /opt/apollo && ./demo.sh start && tail -f /dev/null