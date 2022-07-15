# 基础镜像，必须第一个写
FROM centos:7
# 作者
LABEL maintainer="koushenhai"
# 介绍
LABEL description="jdk 1.8"
# 在当前目录的文件，拷贝过去会自动解压到指定的目录
ADD jdk-8u161-linux-x64.tar.gz /usr/local
# 运行命令
RUN cd /usr/local && mv jdk1.8.0_161 /usr/local/jdk
# 设置环境变量
ENV JAVA_HOME /usr/local/jdk
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin
