# Modbus TCP/UDP Server Docker Image

基于 pymodbus 库构建的 Modbus 服务器模拟器，支持 TCP 和 UDP 协议。

## 构建镜像

```bash
cd laokou-common/laokou-common-testcontainers/docker
sudo docker login
sudo docker build -t laokou/modbus-sim:1.0 .
sudo docker tag laokou/modbus-sim:1.0 koushenhai/modbus-sim
sudo docker push koushenhai/modbus-sim:latest
```

## 运行容器

### TCP 模式（默认）
```bash
docker run -d --name modbus-tcp -p 502:502 laokou/modbus-sim:1.0
```

### UDP 模式
```bash
docker run -d --name modbus-udp -p 502:502/udp laokou/modbus-sim:1.0 --protocol udp --port 502
```

### 自定义端口
```bash
# TCP on port 5020
docker run -d --name modbus-tcp -p 5020:5020 laokou/modbus-sim:1.0 --protocol tcp --port 5020

# UDP on port 5020
docker run -d --name modbus-udp -p 5020:5020/udp laokou/modbus-sim:1.0 --protocol udp --port 5020
```

## 数据块配置

| 类型 | 数量 | 初始值 |
|------|------|--------|
| Coils | 100 | 全部为 True |
| Discrete Inputs | 100 | 交替 True/False |
| Input Registers | 100 | 0-99 |
| Holding Registers | 100 | 100-199 |

## 测试

```bash
# 使用 modbus 客户端工具测试
# 例如使用 modpoll 或 pymodbus-console
```
