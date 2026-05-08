# 边缘网关

## 快速启动

```shell
sudo apt install luajit
sudo apt install lua-cjson
sudo apt install libluajit-5.1-dev
```

```shell
go mod tidy
cd cmd/server
go run main.go -c configs
```

## 项目说明

[标准 Go 项目布局](https://github.com/golang-standards/project-layout)
