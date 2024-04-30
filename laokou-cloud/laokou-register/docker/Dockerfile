# /**
#  * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
LABEL description="laokou-register"
COPY laokou-register.jar app.jar
EXPOSE 8848
# >/dev/null 2>&1 标准输出和错误输出重定向nacos.log
CMD java $JVM_OPTS $NACOS_OPTS -jar app.jar >/opt/nacos.log 2>&1
