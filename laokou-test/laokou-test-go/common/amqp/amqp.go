package amqp

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
		log.Printf("错误信息：%s，错误异常栈：%s", msg, err.Error())
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

func DeclareQueue(channel *amqp.Channel) amqp.Queue {
	queue, err := channel.QueueDeclare(
		"",    // name
		false, // durable
		false, // delete when unused
		true,  // exclusive
		false, // no-wait
		nil,   // arguments
	)
	FailOnError(err, "Failed to declare a queue")
	return queue
}

func BindQueue(channel *amqp.Channel, exchange string, key string, queue amqp.Queue) {
	err := channel.QueueBind(
		queue.Name, // queue name
		key,        // routing key
		exchange,   // exchange
		false,
		nil)
	FailOnError(err, "Failed to bind a queue")
}

func Receive(channel *amqp.Channel, queue amqp.Queue) {
	ms, err := channel.Consume(
		queue.Name, // queue
		"",         // consumer
		false,      // no auto ack
		false,      // exclusive
		false,      // no local
		false,      // no wait
		nil,        // args
	)
	FailOnError(err, "Failed to register a consumer")
	// 协程
	var forever chan struct{}
	go func() {
		for m := range ms {
			log.Printf("接收消息：%s", m.Body)
			// 失败 -> nack
			// err := channel.Nack(m.DeliveryTag, false, true)
			// 成功 -> ack
			err := channel.Ack(m.DeliveryTag, false)
			FailOnError(err, "Failed to ack")
		}
	}()
	<-forever
}

func ModifyQos(channel *amqp.Channel) {
	err := channel.Qos(1, 0, false)
	FailOnError(err, "Failed to modify qos")
}
