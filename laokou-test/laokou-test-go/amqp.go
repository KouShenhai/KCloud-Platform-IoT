package main

import (
	"context"
	"fmt"
	amqp "github.com/rabbitmq/amqp091-go"
	"log"
	"time"
)

type AMQP struct {
	HOST     string
	PORT     int
	USERNAME string
	PASSWORD string
}

func FailOnError(err error, msg string) {
	if err != nil {
		log.Printf("错误信息：%s，错误异常栈：%s", msg, err)
	}
}

func InitAMQP(mq AMQP) *amqp.Connection {
	url := fmt.Sprintf("amqp://%s:%s@%s:%d", mq.USERNAME, mq.PASSWORD, mq.HOST, mq.PORT)
	conn, err := amqp.Dial(url)
	FailOnError(err, "Failed to connect to RabbitMQ")
	return conn
}

func InitChannel(conn *amqp.Connection, exchange string) *amqp.Channel {
	channel, err := conn.Channel()
	FailOnError(err, "Failed to open a channel")
	err = channel.ExchangeDeclare(
		exchange, // name
		"topic",  // type
		true,     // durable
		false,    // auto-deleted
		false,    // internal
		false,    // no-wait
		nil,      // arguments
	)
	FailOnError(err, "Failed to declare an exchange")
	return channel
}

func Send(channel *amqp.Channel, exchange string, key string, payload string) {
	// 5秒内没完成则取消
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	err := channel.PublishWithContext(ctx,
		exchange, // exchange
		key,      // routing key
		false,    // mandatory
		false,    // immediate
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(payload),
		})
	FailOnError(err, "Failed to publish a message")
}

func CloseAMQP(conn *amqp.Connection) {
	if conn != nil {
		err := conn.Close()
		if err != nil {
			return
		}
	}
}

func CloseChannel(channel *amqp.Channel) {
	if channel != nil {
		err := channel.Close()
		if err != nil {
			return
		}
	}
}

func DeclareQueue(channel *amqp.Channel, topic string) {
	_, err := channel.QueueDeclare(
		topic, // name
		false, // durable
		false, // delete when unused
		true,  // exclusive
		false, // no-wait
		nil,   // arguments
	)
	FailOnError(err, "Failed to declare a queue")
}

func BindQueue(channel *amqp.Channel, exchange string, key string, topic string) {
	err := channel.QueueBind(
		topic,    // queue name
		key,      // routing key
		exchange, // exchange
		false,
		nil)
	FailOnError(err, "Failed to bind a queue")
}

func Receive(channel *amqp.Channel, topic string, name string) {
	ms, err := channel.Consume(
		topic, // queue
		"",    // consumer
		true,  // auto ack
		false, // exclusive
		false, // no local
		false, // no wait
		nil,   // args
	)
	FailOnError(err, "Failed to register a consumer")
	// 协程
	var forever chan struct{}
	go func() {
		for m := range ms {
			log.Printf("接收消息：%s", m.Body)
		}
	}()
	<-forever
}

func main() {
	// https://github.com/rabbitmq/rabbitmq-tutorials/blob/main/go/emit_log_topic.go
	exchange := "laokou_iot_exchange"
	mq := AMQP{"127.0.0.1", 5672, "root", "laokou123"}
	payload := "hello world"
	key := "laokou.iot"
	router := "*.iot"
	topic := "laokou_iot_topic"
	name := "laokou_consumer"
	conn := InitAMQP(mq)
	channel := InitChannel(conn, exchange)
	DeclareQueue(channel, topic)
	BindQueue(channel, exchange, router, topic)
	Send(channel, exchange, key, payload)
	Receive(channel, topic, name)
	defer CloseAMQP(conn)
	defer CloseChannel(channel)
}
