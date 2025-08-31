FROM docker.elastic.co/elasticsearch/elasticsearch:8.19.3
LABEL maintainer="laokou"
LABEL description="elasticsearch8"
RUN elasticsearch-plugin install --batch https://release.infinilabs.com/analysis-ik/stable/elasticsearch-analysis-ik-8.19.3.zip && \
    elasticsearch-plugin install --batch https://release.infinilabs.com/analysis-pinyin/stable/elasticsearch-analysis-pinyin-8.19.3.zip
