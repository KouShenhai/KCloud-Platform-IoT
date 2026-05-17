# 边缘网关

## 快速启动

```shell
go mod tidy
```

#### Windows
```shell
cd cmd/server
go run main.go -c D:\laokou\KCloud-Platform-IoT\KEdge-Gateway\configs
go build -o app.exe main.go
app.exe -c D:\laokou\KCloud-Platform-IoT\KEdge-Gateway\configs
```

#### Linux
```shell
cd cmd/server
go run main.go -c /home/a
go build -o app main.go
chmod +x app
./app -c /home/a
```

#### 插件
```shell
# 安装tinygo
tinygo build -o mqtt_parser.wasm -target=wasi main.go
```

## 项目说明

[标准 Go 项目布局](https://github.com/golang-standards/project-layout)
