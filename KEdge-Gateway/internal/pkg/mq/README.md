```go
	mq.InitRabbitMQ(cfg.MQ.RabbitMQ)
	err = mq.RabbitCli.SetupTopology("iot.device.exchange", "topic", "iot.device.properties.queue", "iot.device.*.properties", "", "")
	if err != nil {
		config.Logger.Error("RabbitMQ setup topology error:", zap.Error(err))
	}
	// ==========================================
	// 2. 发送消息阶段 (使用独立的短生命周期 Context)
	// ==========================================
	// 最佳实践：将 pubCtx 限制在块作用域或独立函数中
	func() {
		pubCtx, pubCancel := context.WithTimeout(context.Background(), 3*time.Second)
		defer pubCancel() // 发送完毕后立刻释放资源

		err = mq.RabbitCli.Publish(pubCtx, "iot.device.exchange", "iot.device.1.properties", []byte("hello world"))
		if err != nil {
			config.Logger.Error("RabbitMQ publish error:", zap.Error(err))
		} else {
			config.Logger.Info("Publish success!")
		}
	}()

	// ==========================================
	// 3. 消费消息阶段 (使用长生命周期 Context)
	// ==========================================
	time.Sleep(5 * time.Second) // 模拟你原来的休眠逻辑

	// 这里使用一个不会自动超时的 Context 来管理消费者
	consumeCtx, consumeCancel := context.WithCancel(context.Background())
	defer consumeCancel()
	// 当服务准备关机时，调用 consumeCancel() 即可优雅退出消费者
	// defer consumeCancel() // 如果在 main 函数中，通常结合 os.Signal 监听来调用

	mq.RabbitCli.Consume(consumeCtx, "iot.device.properties.queue", func(body []byte) error {
		config.Logger.Info("receive message:", zap.String("message", string(body)))
		return nil
	})

	// ==========================================
	// 4. 阻塞主协程（非常关键）
	// ==========================================
	// 如果这段代码在 main() 最后，必须加阻塞代码防止程序直接退出
	// 实际生产环境通常是用 <-make(chan os.Signal, 1) 监听系统结束信号
	select {}
```
