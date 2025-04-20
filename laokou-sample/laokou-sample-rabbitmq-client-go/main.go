package main

import (
	"fmt"
	"github.com/rabbitmq/amqp091-go"
	"log"
)

func main() {
	// 连接 RabbitMQ 服务器
	conn, err := amqp091.Dial("amqp://root:laokou123@rabbitmq:5672/")
	if err != nil {
		panic(err)
	}
	defer conn.Close()
	// 创建一个 Channel
	ch, err := conn.Channel()
	if err != nil {
		log.Fatalf("Failed to create a channel: %s", err)
	}
	defer ch.Close()
	// 声明一个队列
	queue, err := ch.QueueDeclare(
		"laokou-queue", // 队列名
		true,           // 是否持久化
		false,          // 是否自动删除
		false,          // 是否独占
		false,          // 是否阻塞
		nil,            // 额外的参数
	)
	if err != nil {
		log.Fatalf("Failed to declare a queue: %s", err)
	}
	// 接收消息
	msgs, err := ch.Consume(
		queue.Name, // 队列名
		"",         // 消费者名
		true,       // 是否自动应答
		false,      // 是否独占
		false,      // 是否阻塞
		false,      // 是否消费者应答
		nil,        // 额外的参数
	)
	if err != nil {
		log.Fatalf("Failed to register a consumer: %s", err)
	}
	// 循环接收消息
	for msg := range msgs {
		fmt.Printf("Received a message: %s\n", msg.Body)
	}
}
