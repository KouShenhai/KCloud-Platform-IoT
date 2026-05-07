# 边缘网关

## 快速启动

```shell
# 编译LuaJIT源码
git clone https://github.com/LuaJIT/LuaJIT.git
cd LuaJIT
make
sudo make install
luajit -v
```

```shell
go mod tidy
cd cmd/server
go run main.go -c configs
```

## 项目说明

[标准 Go 项目布局](https://github.com/golang-standards/project-layout)
