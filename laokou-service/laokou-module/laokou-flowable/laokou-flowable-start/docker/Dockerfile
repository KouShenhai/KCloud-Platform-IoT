# /**
#  * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
FROM docker.io/sunrdocker/jdk21-jre-font-openssl-alpine:latest
LABEL maintainer="laokou"
LABEL description="laokou-flowable"
RUN echo "http://mirrors.aliyun.com/alpine/v3.6/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.6/community" >> /etc/apk/repositories \
    && set -xe \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && apk --no-cache add ttf-dejavu fontconfig \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone
COPY laokou-flowable-start.jar app.jar
COPY ./windows-fonts /usr/share/fonts/ttf-dejavu
EXPOSE 10000
CMD java $JVM_OPTS -jar app.jar >/dev/null 2>&1