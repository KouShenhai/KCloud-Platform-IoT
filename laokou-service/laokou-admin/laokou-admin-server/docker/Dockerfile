FROM docker.io/sunrdocker/jdk17-jre-font-openssl-alpine:latest
LABEL maintainer="koushenhai"
LABEL description="laokou-admin-server"
COPY laokou-admin-server.jar app.jar
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 9999
ENTRYPOINT java -Xms512m -Xmx512m --add-opens=java.base/java.lang=ALL-UNNAMED \
 -Ddruid.filters=mergeStat \
 -Ddruid.useGlobalDataSourceStat=true \
  -Denv=DEV \
  -Dapollo.configService=http://124.222.196.51:8080 \
  -jar app.jar > app.log