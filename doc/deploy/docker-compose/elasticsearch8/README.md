```shell
docker run -d --restart=always   --name elasticsearch -p 9200:9200 -p 9300:9300 --privileged  -e "discovery.type=single-node" -e "ES_JAVA_OPTS=-Xms1g -Xmx1g" -e "ELASTIC_PASSWORD=laokou123" docker.elastic.co/elasticsearch/elasticsearch:8.19.1

docker cp elasticsearch:/usr/share/elasticsearch/data elasticsearch/
docker cp elasticsearch:/usr/share/elasticsearch/plugins elasticsearch/
docker cp elasticsearch:/usr/share/elasticsearch/config elasticsearch/
docker cp elasticsearch:/usr/share/elasticsearch/logs elasticsearch/
```
