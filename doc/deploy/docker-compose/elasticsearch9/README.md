```shell
sudo docker run -d --restart=always   --name elasticsearch -p 9200:9200 -p 9300:9300 --privileged  -e "discovery.type=single-node" -e "ES_JAVA_OPTS=-Xms1g -Xmx1g" -e "ELASTIC_PASSWORD=laokou123" docker.elastic.co/elasticsearch/elasticsearch:9.1.3

sudo docker cp elasticsearch:/usr/share/elasticsearch/data elasticsearch/
sudo docker cp elasticsearch:/usr/share/elasticsearch/plugins elasticsearch/
sudo docker cp elasticsearch:/usr/share/elasticsearch/config elasticsearch/
sudo docker cp elasticsearch:/usr/share/elasticsearch/logs elasticsearch/
```

```shell
sudo docker build -t elasticsearch9:9.1.3 .
sudo docker tag xxx registry.cn-shenzhen.aliyuncs.com/koushenhai/elasticsearch9:9.1.3
sudo docker push registry.cn-shenzhen.aliyuncs.com/koushenhai/elasticsearch9:9.1.3
```

```shell
sudo docker build -t elasticsearch913:9.1.3 .
sudo docker tag elasticsearch913:9.1.3 koushenhai/elasticsearch9
sudo docker push koushenhai/elasticsearch9:latest
```

```shell
sudo vim /etc/docker/daemon.json

[
	"dns": ["8.8.8.8", "8.8.4.4"]
]

sudo systemctl daemon-reload
sudo systemctl restart docker
```
