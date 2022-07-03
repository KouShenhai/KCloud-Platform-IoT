FROM jdk:1.8
LABEL maintainer="koushenhai"
LABEL description="laokou-admin"
COPY sso.jar /opt/sso.jar
COPY apr-1.7.0.tar.gz /opt/apr-1.7.0.tar.gz
COPY apr-iconv-1.2.2.tar.gz /opt/apr-iconv-1.2.2.tar.gz
COPY apr-util-1.6.1.tar.gz /opt/apr-util-1.6.1.tar.gz
COPY tomcat-native-1.2.33-src.tar.gz /opt/tomcat-native-1.2.33-src.tar.gz
ENV LD_LIBRARY_PATH /usr/local/apr/lib
RUN yum -y install gcc pcre pcre-devel zlib zlib-devel openssl openssl-devel expat-devel \
    && cd /opt \
    && tar -xvzf /opt/apr-1.7.0.tar.gz \
    && cd apr-1.7.0 \
    && ./configure --prefix=/usr/local/apr/lib \
    && make \
    && make install \
    && cd /opt \
    && tar -xvzf /opt/apr-iconv-1.2.2.tar.gz \
    && cd apr-iconv-1.2.2 \
    && ./configure --with-apr=/usr/local/apr/lib --with-java-home=/usr/local/jdk \
    && make \
    && make install \
    && cd /opt \
    && tar -xvzf /opt/apr-util-1.6.1.tar.gz \
    && cd apr-util-1.6.1 \
    && ./configure --with-apr=/usr/local/apr/lib --with-java-home=/usr/local/jdk \
    && make \
    && make install \
    && cd /opt \
    && tar -xvzf /opt/tomcat-native-1.2.33-src.tar.gz \
    && cd tomcat-native-1.2.33-src/native \
    && ./configure --with-apr=/usr/local/apr/lib --with-java-home=/usr/local/jdk \
    && make \
    && make install
EXPOSE 8016
VOLUME /data
ENTRYPOINT java -Xms256m -Xmx512m -Ddruid.filters=mergeStat -Ddruid.useGlobalDataSourceStat=true -Djava.library.path=/usr/local/apr/lib -Djasypt.encryptor.password=koushenhai5201314wumeihua -Denv=DEV -Dapollo.configService=http://192.168.62.132:8080 -jar /opt/sso.jar > /opt/sso.log