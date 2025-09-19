FROM docker.elastic.co/elasticsearch/elasticsearch:9.1.4
LABEL maintainer="laokou"
LABEL description="elasticsearch9"
RUN elasticsearch-plugin install --batch https://release.infinilabs.com/analysis-ik/stable/elasticsearch-analysis-ik-9.1.4.zip && \
    elasticsearch-plugin install --batch https://release.infinilabs.com/analysis-pinyin/stable/elasticsearch-analysis-pinyin-9.1.4.zip
