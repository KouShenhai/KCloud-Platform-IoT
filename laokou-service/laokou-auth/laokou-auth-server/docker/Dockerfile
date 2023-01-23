FROM docker.io/sunrdocker/jdk17-jre-font-openssl-alpine:latest
LABEL maintainer="koushenhai"
LABEL description="laokou-auth-server"
COPY laokou-auth-server.jar app.jar
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime  \
    && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 10000
ENTRYPOINT java -Xms512m -Xmx512m --add-opens \
                                  java.base/java.lang=ALL-UNNAMED \
                                -Ddruid.filters=mergeStat -Ddruid.useGlobalDataSourceStat=true \
                                -Djava.awt.headless=true \
                                -Denv=DEV \
                                -jar \
                                app.jar > app.log