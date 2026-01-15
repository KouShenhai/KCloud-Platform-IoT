FROM docker.elastic.co/elasticsearch/elasticsearch:9.2.4
LABEL maintainer="laokou"
LABEL description="elasticsearch9"
RUN elasticsearch-plugin install --batch https://release.infinilabs.com/analysis-ik/stable/elasticsearch-analysis-ik-9.2.4.zip && \
    elasticsearch-plugin install --batch https://release.infinilabs.com/analysis-pinyin/stable/elasticsearch-analysis-pinyin-9.2.4.zip
