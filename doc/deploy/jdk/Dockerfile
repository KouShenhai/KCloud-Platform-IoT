# /*
#  * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
#  * <p>
#  * Licensed under the Apache License, Version 2.0 (the "License");
#  * you may not use this file except in compliance with the License.
#  * You may obtain a copy of the License at
#  * <p>
#  *   http://www.apache.org/licenses/LICENSE-2.0
#  * <p>
#  * Unless required by applicable law or agreed to in writing, software
#  * distributed under the License is distributed on an "AS IS" BASIS,
#  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  * See the License for the specific language governing permissions and
#  * limitations under the License.
#  */
# 基础镜像，必须第一个写
FROM centos:7
# 作者
LABEL maintainer="koushenhai"
# 介绍
LABEL description="jdk 17"
# 在当前目录的文件，拷贝过去会自动解压到指定的目录
COPY jdk-17_linux-x64_bin.tar.gz /opt/
# 运行命令
RUN cd /opt && tar -zxvf  jdk-17_linux-x64_bin.tar.gz \
    && mv jdk-17.0.4.1 /usr/local/jdk
# 安装支持的字体
RUN yum install fontconfig -y
# 设置环境变量
ENV JAVA_HOME /usr/local/jdk
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin
# 设置中文支持
# setup language 解决中文乱码
ENV  LANG C.UTF-8