#### 测试

```shell
pm := engine.NewPluginManager(context.Background())
	// 加载插件
	err = pm.Load(
		"mqtt",
		"D:\\laokou\\KCloud-Platform-IoT\\KEdge-Gateway\\pkg\\plugins\\mqtt\\mqtt_parser.wasm",
	)
	if err != nil {
		panic(err)
	}
	input := `
	{
		"topic":"device/test",
		"payload":"{\"temp\":22.5,\"hum\":60}"
	}
	`
	// 调用插件
	out, err := pm.Call(
		"mqtt",
		"parse",
		[]byte(input),
	)
	if err != nil {
		panic(err)
	}
	config.Logger.Debug(string(out))
	// 卸载插件
	err = pm.Unload("mqtt")
	if err != nil {
		panic(err)
	}
```
